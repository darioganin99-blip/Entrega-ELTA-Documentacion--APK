package com.elta.entregas_doc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final int FILE_CHOOSER_REQUEST_CODE = 101;
    private static final int DOCUMENT_SCANNER_REQUEST_CODE = 102;

    private ValueCallback<Uri[]> uploadMessage;
    private WebView webView;
    private final ArrayList<Uri> selectedDocumentUris = new ArrayList<>();
    private Uri cameraOutputUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(new String[] {
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                }, 100);
            }
        }

        webView = new WebView(this);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setGeolocationEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);

        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return handleExternalUrl(request.getUrl().toString());
                }
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleExternalUrl(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    runOnUiThread(() -> request.grant(request.getResources()));
                }
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                }
                uploadMessage = filePathCallback;
                openScannerChooser(FILE_CHOOSER_REQUEST_CODE);
                return true;
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
        setContentView(webView);
    }

    private boolean handleExternalUrl(String url) {
        if (url == null) return false;
        if (url.startsWith("https://wa.me/") || url.startsWith("http://wa.me/") || url.startsWith("whatsapp://")) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    private Uri createTempImageUri() {
        try {
            File file = new File(getExternalCacheDir(), "scan_" + System.currentTimeMillis() + ".jpg");
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        } catch (Exception e) {
            return null;
        }
    }

    private void openScannerChooser(int requestCode) {
        selectedDocumentUris.clear();

        Intent fileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        fileIntent.setType("*/*");
        fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"application/pdf", "image/*"});
        fileIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        fileIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

        ArrayList<Intent> initialIntents = new ArrayList<>();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraOutputUri = createTempImageUri();
        if (cameraOutputUri != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraOutputUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            initialIntents.add(cameraIntent);
        }

        Intent chooser = Intent.createChooser(fileIntent, "Escanear con cámara o seleccionar PDF");
        if (!initialIntents.isEmpty()) {
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, initialIntents.toArray(new Intent[0]));
        }

        try {
            startActivityForResult(chooser, requestCode);
        } catch (Exception e) {
            if (webView != null) {
                webView.evaluateJavascript("alert('No se encontró una aplicación para escanear o seleccionar documentos.');", null);
            }
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
        }
    }

    public class AndroidBridge {
        @JavascriptInterface
        public void openDocumentScanner() {
            runOnUiThread(() -> openScannerChooser(DOCUMENT_SCANNER_REQUEST_CODE));
        }

        @JavascriptInterface
        public void sendWhatsappWithDocuments(String phone, String message) {
            runOnUiThread(() -> {
                try {
                    Intent intent;

                    if (selectedDocumentUris.size() > 0) {
                        intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        intent.setType("*/*");
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, selectedDocumentUris);
                    } else {
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                    }

                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    if (phone != null && phone.trim().length() > 0) {
                        intent.putExtra("jid", phone.replace("+", "").replace(" ", "") + "@s.whatsapp.net");
                    }

                    intent.setPackage("com.whatsapp");

                    try {
                        startActivity(intent);
                    } catch (Exception e1) {
                        intent.setPackage("com.whatsapp.w4b");
                        try {
                            startActivity(intent);
                        } catch (Exception e2) {
                            intent.setPackage(null);
                            startActivity(Intent.createChooser(intent, "Enviar por WhatsApp"));
                        }
                    }
                } catch (Exception e) {
                    if (webView != null) {
                        webView.evaluateJavascript("alert('No se pudo abrir WhatsApp para enviar adjuntos. Verificá que WhatsApp esté instalado.');", null);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri[] results = extractUris(resultCode, data);

        if (results == null && resultCode == Activity.RESULT_OK && cameraOutputUri != null) {
            results = new Uri[]{cameraOutputUri};
        }

        if (requestCode == FILE_CHOOSER_REQUEST_CODE && uploadMessage != null) {
            uploadMessage.onReceiveValue(results);
            uploadMessage = null;
        }

        if (requestCode == DOCUMENT_SCANNER_REQUEST_CODE) {
            selectedDocumentUris.clear();

            if (results != null && results.length > 0) {
                StringBuilder names = new StringBuilder();

                for (int i = 0; i < results.length; i++) {
                    selectedDocumentUris.add(results[i]);
                    if (i > 0) names.append(",");
                    names.append("Documento ").append(i + 1);
                }

                if (webView != null) {
                    String js = "if(window.onNativeDocumentSelected) window.onNativeDocumentSelected('" + names.toString() + "');";
                    webView.evaluateJavascript(js, null);
                }
            }
        }
    }

    private Uri[] extractUris(int resultCode, Intent data) {
        Uri[] results = null;

        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                results = new Uri[count];
                for (int i = 0; i < count; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    results[i] = uri;
                    try {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception ignored) {}
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                results = new Uri[]{uri};
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception ignored) {}
            }
        }

        return results;
    }
}

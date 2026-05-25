package com.elta.entregas_doc;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int FILE_CHOOSER_REQUEST_CODE = 101;
    private static final int DOCUMENT_SCANNER_REQUEST_CODE = 102;

    private ValueCallback<Uri[]> uploadMessage;
    private WebView webView;
    private final ArrayList<Uri> selectedDocumentUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                if (uploadMessage != null) uploadMessage.onReceiveValue(null);
                uploadMessage = filePathCallback;

                Intent contentIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                contentIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentIntent.setType("*/*");
                contentIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "application/pdf"});
                contentIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                contentIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                contentIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

                try {
                    startActivityForResult(Intent.createChooser(contentIntent, "Seleccionar PDF o imagen"), FILE_CHOOSER_REQUEST_CODE);
                    return true;
                } catch (Exception e) {
                    uploadMessage = null;
                    return false;
                }
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

    public class AndroidBridge {
        @JavascriptInterface
        public void openDocumentScanner() {
            runOnUiThread(() -> startPdfScanner());
        }

        @JavascriptInterface
        public void sendWhatsappWithDocuments(String phone, String message) {
            runOnUiThread(() -> sendWhatsappNative(phone, message));
        }
    }

    private void startPdfScanner() {
        try {
            GmsDocumentScannerOptions options =
                new GmsDocumentScannerOptions.Builder()
                    .setGalleryImportAllowed(true)
                    .setPageLimit(20)
                    .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                    .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                    .build();

            GmsDocumentScanner scanner = GmsDocumentScanning.getClient(options);
            scanner.getStartScanIntent(this)
                .addOnSuccessListener(intentSender -> {
                    try {
                        startIntentSenderForResult(intentSender, DOCUMENT_SCANNER_REQUEST_CODE, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException e) {
                        showScannerError();
                    }
                })
                .addOnFailureListener(e -> showScannerError());
        } catch (Exception e) {
            showScannerError();
        }
    }

    private void showScannerError() {
        if (webView != null) {
            webView.evaluateJavascript("alert('No se pudo abrir el escáner PDF. Verificá Google Play Services.');", null);
        }
    }

    private Uri copyUriToShareCache(Uri sourceUri, String fileName) {
        try {
            File dir = new File(getCacheDir(), "shared_docs");
            if (!dir.exists()) dir.mkdirs();

            File outFile = new File(dir, fileName);
            InputStream in = getContentResolver().openInputStream(sourceUri);
            FileOutputStream out = new FileOutputStream(outFile);

            byte[] buffer = new byte[8192];
            int len;
            while (in != null && (len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            if (in != null) in.close();
            out.close();

            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", outFile);
        } catch (Exception e) {
            return sourceUri;
        }
    }


    private Uri createTextInfoFile(String message) {
        try {
            File dir = new File(getCacheDir(), "shared_docs");
            if (!dir.exists()) dir.mkdirs();

            File outFile = new File(dir, "ELTA_registro.txt");
            OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(outFile),
                StandardCharsets.UTF_8
            );

            writer.write(message == null ? "" : message);
            writer.flush();
            writer.close();

            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", outFile);
        } catch (Exception e) {
            return null;
        }
    }

    private void sendWhatsappNative(String phone, String message) {
        String cleanPhone = phone == null ? "" : phone.replace("+", "").replace(" ", "").replace("-", "").trim();

        copyTextToClipboard(message);

        try {
            if (selectedDocumentUris != null && selectedDocumentUris.size() > 0) {
                ArrayList<Uri> shareUris = new ArrayList<>();

                for (int i = 0; i < selectedDocumentUris.size(); i++) {
                    shareUris.add(copyUriToShareCache(selectedDocumentUris.get(i), "ELTA_documento_" + (i + 1) + ".pdf"));
                }

                // Archivo TXT con toda la información de entrega.
                // WhatsApp a veces ignora EXTRA_TEXT cuando se envían documentos,
                // por eso se adjunta este TXT junto al PDF para no perder datos.
                Uri infoTxt = createTextInfoFile(message);
                if (infoTxt != null) {
                    shareUris.add(infoTxt);
                }

                Intent shareIntent = buildShareIntentWithDocuments(cleanPhone, message, shareUris);

                if (isPackageInstalled("com.whatsapp")) {
                    shareIntent.setPackage("com.whatsapp");
                    startActivity(shareIntent);
                    return;
                }

                if (isPackageInstalled("com.whatsapp.w4b")) {
                    shareIntent.setPackage("com.whatsapp.w4b");
                    startActivity(shareIntent);
                    return;
                }

                shareIntent.setPackage(null);
                startActivity(Intent.createChooser(shareIntent, "Enviar registro con documentos"));
                return;
            }

            openWhatsappText(cleanPhone, message);

        } catch (Exception firstError) {
            try {
                Intent fallback = new Intent(Intent.ACTION_SEND);
                fallback.setType("text/plain");
                fallback.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(fallback, "Enviar registro"));
            } catch (Exception secondError) {
                if (webView != null) {
                    webView.evaluateJavascript(
                        "alert('No se pudo abrir WhatsApp. El texto quedó copiado al portapapeles.');",
                        null
                    );
                }
            }
        }
    }

    private Intent buildShareIntentWithDocuments(String cleanPhone, String message, ArrayList<Uri> shareUris) {
        Intent shareIntent;

        if (shareUris.size() == 1) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, shareUris.get(0));
        } else {
            shareIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, shareUris);
        }

        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, message);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "ELTA - Registro de entrega");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "ELTA - Registro de entrega");

        if (cleanPhone != null && cleanPhone.length() > 0) {
            shareIntent.putExtra("jid", cleanPhone + "@s.whatsapp.net");
        }

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        ClipData clipData = null;
        for (int i = 0; i < shareUris.size(); i++) {
            Uri uri = shareUris.get(i);
            if (i == 0) {
                clipData = ClipData.newUri(getContentResolver(), "Documento", uri);
            } else if (clipData != null) {
                clipData.addItem(new ClipData.Item(uri));
            }
        }

        if (clipData != null) {
            shareIntent.setClipData(clipData);
        }

        grantUrisToCompatibleApps(shareIntent, shareUris);

        return shareIntent;
    }

    private boolean isPackageInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void copyTextToClipboard(String message) {
        try {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("ELTA Registro", message);
            clipboard.setPrimaryClip(clip);
        } catch (Exception ignored) {}
    }

    private void grantUrisToCompatibleApps(Intent intent, ArrayList<Uri> uris) {
        try {
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                for (Uri uri : uris) {
                    grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
        } catch (Exception ignored) {}
    }

    private void openWhatsappText(String cleanPhone, String message) {
        try {
            Intent waIntent = new Intent(Intent.ACTION_VIEW);
            waIntent.setData(Uri.parse("whatsapp://send?phone=" + cleanPhone + "&text=" + Uri.encode(message)));
            startActivity(waIntent);
        } catch (Exception e) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse("https://wa.me/" + cleanPhone + "?text=" + Uri.encode(message)));
            startActivity(webIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_REQUEST_CODE && uploadMessage != null) {
            Uri[] results = extractUris(resultCode, data);
            uploadMessage.onReceiveValue(results);

            selectedDocumentUris.clear();
            if (results != null && results.length > 0) {
                StringBuilder names = new StringBuilder();
                for (int i = 0; i < results.length; i++) {
                    selectedDocumentUris.add(results[i]);
                    if (i > 0) names.append(",");
                    names.append("Documento ").append(i + 1);
                }
                notifyDocumentsSelected(names.toString());
            }
            uploadMessage = null;
        }

        if (requestCode == DOCUMENT_SCANNER_REQUEST_CODE) {
            selectedDocumentUris.clear();
            if (resultCode == Activity.RESULT_OK && data != null) {
                GmsDocumentScanningResult result = GmsDocumentScanningResult.fromActivityResultIntent(data);
                if (result != null && result.getPdf() != null && result.getPdf().getUri() != null) {
                    selectedDocumentUris.add(result.getPdf().getUri());
                    notifyDocumentsSelected("PDF escaneado");
                }
            }
        }
    }

    private void notifyDocumentsSelected(String names) {
        if (webView != null) {
            String safe = names == null ? "" : names.replace("'", "\\'");
            webView.evaluateJavascript("if(window.onNativeDocumentSelected) window.onNativeDocumentSelected('" + safe + "');", null);
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

package com.elta.entregas_doc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;

import java.util.ArrayList;

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

            if (
                    checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED ||

                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED ||

                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        100
                );
            }
        }

        webView = new WebView(this);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setGeolocationEnabled(true);

        webView.addJavascriptInterface(
                new AndroidBridge(),
                "AndroidBridge"
        );

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request
            ) {

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    String url
            ) {

                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onGeolocationPermissionsShowPrompt(
                    String origin,
                    GeolocationPermissions.Callback callback
            ) {

                callback.invoke(origin, true, false);
            }

            @Override
            public void onPermissionRequest(
                    final PermissionRequest request
            ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    runOnUiThread(() ->
                            request.grant(request.getResources())
                    );
                }
            }

            @Override
            public boolean onShowFileChooser(
                    WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams
            ) {

                if (uploadMessage != null) {

                    uploadMessage.onReceiveValue(null);
                }

                uploadMessage = filePathCallback;

                Intent contentIntent =
                        new Intent(Intent.ACTION_OPEN_DOCUMENT);

                contentIntent.addCategory(
                        Intent.CATEGORY_OPENABLE
                );

                contentIntent.setType("*/*");

                contentIntent.putExtra(
                        Intent.EXTRA_MIME_TYPES,
                        new String[]{
                                "image/*",
                                "application/pdf"
                        }
                );

                contentIntent.putExtra(
                        Intent.EXTRA_ALLOW_MULTIPLE,
                        true
                );

                try {

                    startActivityForResult(
                            Intent.createChooser(
                                    contentIntent,
                                    "Seleccionar PDF"
                            ),
                            FILE_CHOOSER_REQUEST_CODE
                    );

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

    public class AndroidBridge {

        @JavascriptInterface
        public void openDocumentScanner() {

            runOnUiThread(() -> startPdfScanner());
        }

        @JavascriptInterface
        public void sendWhatsappWithDocuments(
                String phone,
                String message
        ) {

            runOnUiThread(() ->
                    sendWhatsappNative(phone, message)
            );
        }
    }

    private void startPdfScanner() {

        try {

            GmsDocumentScannerOptions options =
                    new GmsDocumentScannerOptions.Builder()
                            .setGalleryImportAllowed(true)
                            .setPageLimit(20)
                            .setResultFormats(
                                    GmsDocumentScannerOptions.RESULT_FORMAT_PDF
                            )
                            .setScannerMode(
                                    GmsDocumentScannerOptions.SCANNER_MODE_FULL
                            )
                            .build();

            GmsDocumentScanner scanner =
                    GmsDocumentScanning.getClient(options);

            scanner.getStartScanIntent(this)
                    .addOnSuccessListener(intentSender -> {

                        try {

                            startIntentSenderForResult(
                                    intentSender,
                                    DOCUMENT_SCANNER_REQUEST_CODE,
                                    null,
                                    0,
                                    0,
                                    0
                            );

                        } catch (Exception e) {

                            showScannerError();
                        }
                    })
                    .addOnFailureListener(
                            e -> showScannerError()
                    );

        } catch (Exception e) {

            showScannerError();
        }
    }

    private void showScannerError() {

        if (webView != null) {

            webView.evaluateJavascript(
                    "alert('No se pudo abrir el escaner PDF');",
                    null
            );
        }
    }

    private void sendWhatsappNative(
            String phone,
            String message
    ) {

        try {

            // ===== CON DOCUMENTOS =====
            if (
                    selectedDocumentUris != null &&
                    selectedDocumentUris.size() > 0
            ) {

                Intent shareIntent =
                        new Intent(Intent.ACTION_SEND_MULTIPLE);

                shareIntent.setType("*/*");

                shareIntent.putParcelableArrayListExtra(
                        Intent.EXTRA_STREAM,
                        selectedDocumentUris
                );

                shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        message
                );

                shareIntent.addFlags(
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                shareIntent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                );

                Intent chooser = Intent.createChooser(
                        shareIntent,
                        "Enviar por WhatsApp"
                );

                startActivity(chooser);

                return;
            }

            // ===== SOLO TEXTO =====

            String cleanPhone =
                    phone == null
                            ? ""
                            : phone
                            .replace("+", "")
                            .replace(" ", "")
                            .replace("-", "")
                            .trim();

            Intent waIntent =
                    new Intent(Intent.ACTION_VIEW);

            waIntent.setData(
                    Uri.parse(
                            "https://wa.me/"
                                    + cleanPhone
                                    + "?text="
                                    + Uri.encode(message)
                    )
            );

            startActivity(waIntent);

        } catch (Exception e) {

            if (webView != null) {

                webView.evaluateJavascript(
                        "alert('Error al abrir WhatsApp');",
                        null
                );
            }
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                requestCode == FILE_CHOOSER_REQUEST_CODE &&
                uploadMessage != null
        ) {

            Uri[] results = extractUris(
                    resultCode,
                    data
            );

            uploadMessage.onReceiveValue(results);

            uploadMessage = null;
        }

        if (
                requestCode == DOCUMENT_SCANNER_REQUEST_CODE
        ) {

            selectedDocumentUris.clear();

            if (
                    resultCode == Activity.RESULT_OK &&
                    data != null
            ) {

                GmsDocumentScanningResult result =
                        GmsDocumentScanningResult
                                .fromActivityResultIntent(data);

                if (
                        result != null &&
                        result.getPdf() != null &&
                        result.getPdf().getUri() != null
                ) {

                    selectedDocumentUris.add(
                            result.getPdf().getUri()
                    );

                    if (webView != null) {

                        webView.evaluateJavascript(
                                "if(window.onNativeDocumentSelected) window.onNativeDocumentSelected('PDF escaneado');",
                                null
                        );
                    }
                }
            }
        }
    }

    private Uri[] extractUris(
            int resultCode,
            Intent data
    ) {

        Uri[] results = null;

        if (
                resultCode == Activity.RESULT_OK &&
                data != null
        ) {

            if (data.getClipData() != null) {

                int count =
                        data.getClipData().getItemCount();

                results = new Uri[count];

                for (int i = 0; i < count; i++) {

                    Uri uri =
                            data.getClipData()
                                    .getItemAt(i)
                                    .getUri();

                    results[i] = uri;
                }

            } else if (data.getData() != null) {

                Uri uri = data.getData();

                results = new Uri[]{uri};
            }
        }

        return results;
    }
}

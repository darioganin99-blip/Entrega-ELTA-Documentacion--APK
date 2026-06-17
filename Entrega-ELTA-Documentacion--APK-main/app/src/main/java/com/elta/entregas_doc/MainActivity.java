package com.elta.entregas_doc;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
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

import com.tom_roush.pdfbox.android.PDFBoxResourceLoader;
import com.tom_roush.pdfbox.io.MemoryUsageSetting;
import com.tom_roush.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

    private File getSharedDir() {
        File dir = new File(getCacheDir(), "shared_docs");
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private File copyUriToFile(Uri sourceUri, String fileName) {
        try {
            File outFile = new File(getSharedDir(), fileName);
            InputStream in = getContentResolver().openInputStream(sourceUri);
            FileOutputStream out = new FileOutputStream(outFile);

            byte[] buffer = new byte[8192];
            int len;
            while (in != null && (len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            if (in != null) in.close();
            out.flush();
            out.close();

            return outFile;
        } catch (Exception e) {
            return null;
        }
    }

    private File createSummaryPdfFile(String message) {
        try {
            File outFile = new File(getSharedDir(), "ELTA_Resumen_Entrega.pdf");

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Paint titlePaint = new Paint();
            titlePaint.setColor(0xFF111827);
            titlePaint.setTextSize(18);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            Paint bodyPaint = new Paint();
            bodyPaint.setColor(0xFF111827);
            bodyPaint.setTextSize(12);

            int x = 40;
            int y = 50;
            canvas.drawText("ELTA - Registro de Entrega", x, y, titlePaint);
            y += 30;

            String[] lines = (message == null ? "" : message).split("\\n");
            for (String line : lines) {
                if (y > 800) {
                    document.finishPage(page);
                    page = document.startPage(new PdfDocument.PageInfo.Builder(595, 842, document.getPages().size() + 1).create());
                    canvas = page.getCanvas();
                    y = 50;
                }

                List<String> wrapped = wrapLine(line, 85);
                for (String part : wrapped) {
                    canvas.drawText(part, x, y, bodyPaint);
                    y += 18;
                }
            }

            document.finishPage(page);

            FileOutputStream out = new FileOutputStream(outFile);
            document.writeTo(out);
            document.close();
            out.close();

            return outFile;
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> wrapLine(String text, int maxChars) {
        ArrayList<String> result = new ArrayList<>();
        if (text == null) {
            result.add("");
            return result;
        }

        String remaining = text;
        while (remaining.length() > maxChars) {
            int cut = remaining.lastIndexOf(" ", maxChars);
            if (cut <= 0) cut = maxChars;
            result.add(remaining.substring(0, cut));
            remaining = remaining.substring(cut).trim();
        }
        result.add(remaining);
        return result;
    }

    private Uri buildCompleteDeliveryPdf(String message) {
        try {
            PDFBoxResourceLoader.init(getApplicationContext());

            File summaryPdf = createSummaryPdfFile(message);
            if (summaryPdf == null) return null;

            File finalPdf = new File(getSharedDir(), "ELTA_Entrega_Completa.pdf");

            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationFileName(finalPdf.getAbsolutePath());

            // Primero resumen de entrega
            merger.addSource(summaryPdf);

            // Luego PDFs escaneados/adjuntos
            for (int i = 0; i < selectedDocumentUris.size(); i++) {
                File copied = copyUriToFile(selectedDocumentUris.get(i), "ELTA_documento_" + (i + 1) + ".pdf");
                if (copied != null) merger.addSource(copied);
            }

            merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", finalPdf);
        } catch (Exception e) {
            if (webView != null) {
                webView.evaluateJavascript("alert('No se pudo combinar el PDF final.');", null);
            }
            return null;
        }
    }

    private void sendWhatsappNative(String phone, String message) {
        String cleanPhone = phone == null ? "" : phone.replace("+", "").replace(" ", "").replace("-", "").trim();
        copyTextToClipboard(message);

        try {
            if (selectedDocumentUris != null && selectedDocumentUris.size() > 0) {
                Uri finalPdfUri = buildCompleteDeliveryPdf(message);

                if (finalPdfUri != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, finalPdfUri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "ELTA - Registro de entrega");
                    shareIntent.putExtra(Intent.EXTRA_TITLE, "ELTA - Registro de entrega");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setClipData(ClipData.newUri(getContentResolver(), "ELTA_Entrega_Completa.pdf", finalPdfUri));

                    if (cleanPhone.length() > 0) {
                        shareIntent.putExtra("jid", cleanPhone + "@s.whatsapp.net");
                    }

                    grantUriToCompatibleApps(shareIntent, finalPdfUri);

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
                    startActivity(Intent.createChooser(shareIntent, "Enviar registro completo"));
                    return;
                }
            }

            openWhatsappText(cleanPhone, message);

        } catch (Exception firstError) {
            try {
                openWhatsappText(cleanPhone, message);
            } catch (Exception secondError) {
                if (webView != null) {
                    webView.evaluateJavascript("alert('No se pudo abrir WhatsApp.');", null);
                }
            }
        }
    }

    private void grantUriToCompatibleApps(Intent intent, Uri uri) {
        try {
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                grantUriPermission(resolveInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } catch (Exception ignored) {}
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

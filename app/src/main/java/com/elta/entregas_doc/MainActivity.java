private void sendWhatsappNative(String phone, String message) {

    String cleanPhone =
            phone == null
                    ? ""
                    : phone.replace("+", "")
                    .replace(" ", "")
                    .replace("-", "")
                    .trim();

    try {

        // =========================
        // ENVIO CON DOCUMENTOS PDF
        // =========================

        if (
                selectedDocumentUris != null &&
                selectedDocumentUris.size() > 0
        ) {

            Intent shareIntent =
                    new Intent(Intent.ACTION_SEND_MULTIPLE);

            // IMPORTANTE
            shareIntent.setType("*/*");

            // TEXTO
            shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    message
            );

            // DOCUMENTOS PDF
            shareIntent.putParcelableArrayListExtra(
                    Intent.EXTRA_STREAM,
                    selectedDocumentUris
            );

            // CLIP DATA
            shareIntent.setClipData(
                    android.content.ClipData.newUri(
                            getContentResolver(),
                            "Documento",
                            selectedDocumentUris.get(0)
                    )
            );

            // PERMISOS URI
            shareIntent.addFlags(
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            shareIntent.addFlags(
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            );

            shareIntent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
            );

            // SELECTOR
            Intent chooser =
                    Intent.createChooser(
                            shareIntent,
                            "Enviar registro con PDF"
                    );

            startActivity(chooser);

            return;
        }

        // =========================
        // SOLO TEXTO
        // =========================

        openWhatsappText(
                cleanPhone,
                message
        );

    } catch (Exception firstError) {

        try {

            openWhatsappText(
                    cleanPhone,
                    message
            );

        } catch (Exception secondError) {

            try {

                Intent textIntent =
                        new Intent(Intent.ACTION_SEND);

                textIntent.setType("text/plain");

                textIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        message
                );

                startActivity(
                        Intent.createChooser(
                                textIntent,
                                "Enviar registro"
                        )
                );

            } catch (Exception finalError) {

                if (webView != null) {

                    webView.evaluateJavascript(
                            "alert('No se pudo abrir WhatsApp');",
                            null
                    );
                }
            }
        }
    }
}

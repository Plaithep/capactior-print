package com.plaithep.customprint;

import android.content.Context;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@CapacitorPlugin(name = "MyPrinter")
public class MyPrinterPlugin extends Plugin {

    private WebView mWebView; // Keep a reference to the WebView to prevent garbage collection

    private MyPrinter implementation = new MyPrinter();

    @PluginMethod()
    public void print(PluginCall call) {
        String html = call.getString("html");
        String jobName = call.getString("jobName", "My Document");

        if (html == null || html.isEmpty()) {
            call.reject("HTML content is required.");
            return;
        }

        Context context = getContext(); // Use the plugin's context

        // Initialize WebView on the UI thread
        getBridge().getActivity().runOnUiThread(() -> {
            mWebView = new WebView(context);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    createWebPrintJob(call, jobName);
                }
            });

            // Load HTML content into the WebView
            mWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        });
    }

        @PluginMethod()
        public void printBlob(PluginCall call) {
            String base64Data = call.getString("data");
            String type = call.getString("type"); // "image" or "pdf"
            String jobName = call.getString("jobName", "My Document");

            if (base64Data == null || base64Data.isEmpty()) {
                call.reject("Base64 data is required for printing blob.");
                return;
            }
            if (type == null || (!type.equals("image") && !type.equals("pdf"))) {
                call.reject("Blob type must be 'image' or 'pdf'.");
                return;
            }

            byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);

            // This will be used to resolve/reject the plugin call after the print intent finishes
            currentPrintCallbacks = new PrintJobCallbacks() {
                @Override
                public void resolve() {
                    call.resolve();
                    currentPrintCallbacks = null;
                }
                @Override
                public void reject(String message) {
                    call.reject(message);
                    currentPrintCallbacks = null;
                }
            };

            if (type.equals("image")) {
                printImage(decodedBytes, jobName);
            } else if (type.equals("pdf")) {
                printPdf(decodedBytes, jobName);
            }
        }

            private void printPdf(byte[] pdfData, String jobName) {
                getBridge().getActivity().runOnUiThread(() -> {
                    try {
                        // Save PDF to a temporary file
                        File cachePath = new File(getContext().getCacheDir(), "pdfs");
                        cachePath.mkdirs();
                        File tempFile = new File(cachePath, jobName.replaceAll("[^a-zA-Z0-9.-]", "_") + ".pdf");

                        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                            fos.write(pdfData);
                            fos.flush();
                        }

                        // Create a content URI for the file
                        Uri uri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", tempFile);

                        // Create an intent to view/print the PDF
                        Intent printIntent = new Intent(Intent.ACTION_VIEW); // ACTION_VIEW works well for opening/sharing documents
                        printIntent.setDataAndType(uri, "application/pdf");
                        printIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        printIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Optional: clear from history

                        // Start activity with chooser
                        getBridge().getActivity().startActivity(Intent.createChooser(printIntent, jobName));
                        currentPrintCallbacks.resolve(); // Resolve assuming the intent opens

                    } catch (Exception e) {
                        currentPrintCallbacks.reject("Failed to print PDF: " + e.getMessage());
                    }
                });
            }


    private void createWebPrintJob(PluginCall call, String jobName) {
        PrintManager printManager = (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);

        if (printManager == null) {
            call.reject("Print service not available.");
            return;
        }

        PrintDocumentAdapter printAdapter;
        printAdapter = mWebView.createPrintDocumentAdapter(jobName);

        printManager.print(jobName, printAdapter, null);
        call.resolve();
        mWebView = null; // Release WebView after print job initiated
    }
}

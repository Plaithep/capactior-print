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

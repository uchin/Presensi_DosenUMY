package com.bsi.presensidosen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PrivacyPolicy extends AppCompatActivity {

    private WebView webview;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;
    private ProgressBar progressBarWebs;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_privacy_policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(R.string.PP);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBarWebs = (ProgressBar) findViewById(R.id.progressBarWebpp);
        webview = (WebView) findViewById(R.id.webViewPrivasi);

        if (savedInstanceState != null) {
            webview.restoreState(savedInstanceState);
        } else {
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setSupportZoom(false);
            settings.setBuiltInZoomControls(false);
            settings.setLoadWithOverviewMode(true);
            webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webview.setWebViewClient(new ourViewClient());

            webview.loadUrl("file:///android_asset/kebijakanprivasi.html");

            webview.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBarWebs.setProgress(progress);
                    if (progress < 100 && progressBarWebs.getVisibility() == View.GONE) {
                        progressBarWebs.setVisibility(View.VISIBLE);
                    }
                    if (progress == 100) {
                        progressBarWebs.setVisibility(View.GONE);
                    }

                }

            });
        }
    }

    private class ourViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView v, String url) {
            if (url.contains("umy.ac.id")) {
                v.loadUrl(url);
                CookieManager.getInstance().setAcceptCookie(true);
            } else {
                Uri uri = Uri.parse(url);
                startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, uri), "Pilih Browser..."));
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webview.saveState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}

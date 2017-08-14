package com.colingo.webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.app.R;

public class ShowWebViewActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_main);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBlockNetworkImage(false);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("http://gw.cs.cn/jx.html");
    }

    // Prevent the back-button from closing the app
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
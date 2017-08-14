package com.demo.colingo.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.colingo.webview.ShowWebViewActivity;
import com.example.app.R;

public class MainActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mWebView = (WebView) findViewById(R.id.imgWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.loadUrl("http://sc.cs.cn/maingif.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(MainActivity.this, ShowWebViewActivity.class);
                startActivity(webView);
                return true;
            }
        });


    }
}

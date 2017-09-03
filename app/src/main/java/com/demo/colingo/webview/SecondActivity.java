package com.demo.colingo.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.colingo.webview.ShowWebViewActivity;
import com.example.app.R;

public class SecondActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        mWebView = (WebView) findViewById(R.id.imgWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.loadUrl("http://sc.cs.cn/maingif.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(SecondActivity.this, ShowWebViewActivity.class);
                startActivity(webView);
                return true;
            }
        });


    }
}

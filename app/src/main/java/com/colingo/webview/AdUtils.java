package com.colingo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.app.R;

/**
 * Created by colingo on 16/9/2017.
 */

public abstract class AdUtils {

    public static void showAd(View view, final Context context) {
        WebView mWebView = (WebView)view.findViewById(R.id.imgWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.loadUrl("http://sc.cs.cn/maingif.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(context, ShowWebViewActivity.class);
                context.startActivity(webView);
                return true;
            }
        });
    }
}

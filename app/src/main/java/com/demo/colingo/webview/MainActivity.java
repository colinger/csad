package com.demo.colingo.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.colingo.webview.ShowWebViewActivity;
import com.example.app.R;

public class MainActivity extends Activity {

    private WebView mWebView;
    private WindowManager wm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = new WebView(this){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                switch (event.getKeyCode()) {

                    case KeyEvent.KEYCODE_BACK:
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        } else {
                            onBackPressed();
                        }
                        break;
                    default:

                        break;

                }
                return super.dispatchKeyEvent(event);
            }
        };
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.loadUrl("http://sc.cs.cn/maingif.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(MainActivity.this, ShowWebViewActivity.class);
                startActivity(webView);
                return true;
            }
        });

        final float scale = getResources().getDisplayMetrics().density;
        int n = (int)(70 * scale + 0.5f);
        wm=(WindowManager)getApplicationContext().getSystemService("window");
        WindowManager.LayoutParams wmlay = new WindowManager.LayoutParams();
        wmlay.type= WindowManager.LayoutParams.TYPE_PHONE;                      //当前悬浮窗口位于phone层
        wmlay.format= PixelFormat.RGBA_8888;                      //悬浮窗口背景设为透明
        wmlay.gravity=Gravity.RIGHT| Gravity.CENTER_VERTICAL;
        wmlay.width= n;
        wmlay.height= n;
        wmlay.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;	//属性设置
        wmlay.x = 0;
        wmlay.y = 0;
        wm.addView(mWebView, wmlay);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wm.removeView(mWebView);
    }
}

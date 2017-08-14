package com.demo.colingo.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

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
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        String imageSrcHtml = "<html>\n" +
                " <head>\n" +
                "  <script bottom=\"65px\" top=\"20px\" right=\"20px\" left=\"\" appentrance=\"1\" width=\"65px\" height=\"auto\" business=\"money\">\n" +
                "  \tfunction showH5(){\n" +
                "\t\tAndroid.startNewActivity();\n" +
                "\t}\n" +
                "  </script>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <div id=\"creatDiv\" style=\"width:70px;height:auto;top:52.266666666666666px;position:fixed;left:;bottom:169.86666666666667px;right:52.266666666666666px;z-index: 9999999;\">\n" +
                "   <a id=\"creatA\" href=\"javascript:;\" style=\"width:100%;height:auto;-webkit-tap-highlight-color:transparent;display:block;\" onclick=\"showH5();\"><img src=\"http://gw.cs.cn/dongtai.gif\" style=\"width:100%;height:auto;\" /></a>\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>";
        mWebView.loadData(imageSrcHtml, "text/html", "UTF-8");


    }

    class WebAppInterface{
        Context context;

        public WebAppInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void startNewActivity(){
            Intent webView = new Intent(this.context, ShowWebViewActivity.class);
            startActivity(webView);
        }
    }
}

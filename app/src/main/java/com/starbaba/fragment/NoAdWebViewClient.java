package com.starbaba.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by colingo on 22/11/2017.
 */

public class NoAdWebViewClient extends WebViewClient {
    private Context context;
    private WebView webView;
    private boolean isClose;

    public NoAdWebViewClient(Context context,WebView webView) {
        this.context = context;
        this.webView = webView;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if(isClose){ //如果线程正在运行就不用重新开启一个线程了
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isClose = true;
                while (isClose){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0x001);
                }
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String js = getClearAdDivJs(context);
            Log.v("adJs",js);
            webView.loadUrl(js); //加载js方法代码
            webView.loadUrl("javascript:hideAd();"); //调用js方法
        }
    };

    private String getClearAdDivJs(Context context) {
        String js = "javascript:function hideAd() {";
        Resources res = context.getResources();
        String[] adDivs = new String[]{"ina_fixed_bottom","ina_buy_icon"};
        for (int i = 0; i < adDivs.length; i++) {
            //通过div的id属性删除div元素
            //js += "var adDiv"+i+"= document.getElementById('"+adDivs[i]+"');if(adDiv"+i+" != null)adDiv"+i+".parentNode.removeChild(adDiv"+i+");";
            //通过div的class属性隐藏div元素
            js += "var adDiv" + i + "= document.getElementsByClassName('" + adDivs[i] + "');if(adDiv" + i + " != null)" +
                    "{var x; for (x = 0; x < adDiv" + i + ".length; x++) {adDiv" + i + "[x].style.display='none';}}";
        }
        js += "}";
        return js;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        isClose = false;
    }
}

package com.baidu.kfk.wv;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 *
 */

public abstract class AdUtils {
    private static WebView  mWebView;
    private static Runnable switchThread = new Runnable() {

                                             @Override
                                             public void run() {
                                                 boolean isShow = checkSwitch();
                                                 if (isShow) {
                                                     mHandler.sendEmptyMessage(0);
                                                 } else {
                                                     mHandler.sendEmptyMessage(1);
                                                 }
                                             }
                                         };
    private static Handler  mHandler     = new Handler() {

                                             @Override
                                             public void handleMessage(Message msg) {
                                                 super.handleMessage(msg);
                                                 if (msg.what == 0) {
                                                     mWebView.setVisibility(View.VISIBLE);
                                                 } else {
                                                     mWebView.setVisibility(View.GONE);
                                                 }

                                             }

                                         };

    public static void showAd(View view, final Context context) {
        //
        requestWrite(context);
        //
        mWebView = (WebView) view.findViewById(R.id.imgWebView);
        if (mWebView == null) {
            Log.e("XXX", view.getId() + "_" + R.id.imgWebView);
        } else {
            //
            Log.e("YYY", view.getId() + "_" + R.id.imgWebView);
        }
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.loadUrl("http://sc.cs.cn/maingif.html");
        mWebView.setVisibility(View.GONE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(context, ShowWebViewActivity.class);
                context.startActivity(webView);
                return true;
            }
        });
        //
        Thread mThread = new Thread(switchThread);
        mThread.start();
    }

    /**
     *
     * @param context
     */
    public static void requestWrite(final Context context) {
        if (PermissionsUtil.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        } else {
            PermissionsUtil.requestPermission(context, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private static boolean checkSwitch() {
        try {

            StringBuffer sb = new StringBuffer();
            URL url = new URL("http://sc.cs.cn/showad.php?appId=111");
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            //设置超时时间
            urlConn.setConnectTimeout(1000);
            //链接状态
            int code = urlConn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                //得到读取的内容(流)  
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
                // 为输出创建BufferedReader  
                BufferedReader buffer = new BufferedReader(in);
                String inputLine = null;
                //使用循环来读取获得的数据  
                while (((inputLine = buffer.readLine()) != null)) {
                    //我们在每一行后面加上一个"\n"来换行  
                    sb.append(inputLine);
                }
                //关闭InputStreamReader  
                in.close();
            }
            //关闭http连接  
            urlConn.disconnect();
            //
            if (sb.toString().toLowerCase().equals("true")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
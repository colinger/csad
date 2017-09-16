package com.demo.colingo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.colingo.webview.ShowWebViewActivity;
import com.example.app.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gzbd_kfb_002 on 2016/9/6 0006. 自定义带动画的Toast
 */
public class UIDialog {

    private static boolean             mIsShow = false;
    private WindowManager              mWdm    = null;
    private final View                 mToastView;
    private WindowManager.LayoutParams mParams;
    private final WebView              mWebView;

    int                                i       = 20;
    private final Timer                mTimer2;
    Handler                            handler = new Handler(Looper.getMainLooper()) {
                                                   @Override
                                                   public void handleMessage(Message msg) {
                                                       super.handleMessage(msg);
                                                       --i;
                                                       if (i > 0) {
                                                           //                time.setText(i + "S");
                                                       } else {
                                                           mTimer2.cancel();
                                                           mWdm.removeView(mToastView);
                                                           mIsShow = false;
                                                       }
                                                   }
                                               };

    public static UIDialog makeText(Context context) {
        if (mIsShow) {
            return null;
        }

        UIDialog result = new UIDialog(context);
        return result;
    }

    private UIDialog(final Context context) {

        i = 200;
        //通过Toast实例获取当前android系统的默认Toast的View布局
        mWdm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mToastView = View.inflate(context.getApplicationContext(), R.layout.alert_window_menu, null);

        mWebView = (WebView) mToastView.findViewById(R.id.imgWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.loadUrl("http://sc.cs.cn/maingif.html");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent();
                intent.setClass(context, ShowWebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
//                mWdm.removeView(mToastView);
                return true;
            }
        });

        mTimer2 = new Timer();

        //设置布局参数
        setParams();
    }

    /**
     * @param
     */
    private void setParams() {
        mParams = new WindowManager.LayoutParams();
        //     mParams.height = height;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //   4.4以后 管理软件可以关闭弹窗 ，TYPE_TOAST 不需要权限
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;

        } else {
            //4.4 以前TYPE_TOAST 不能接受事件，TYPE_SYSTEM_ALERT 可以
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        //      | WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        mParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
    }

    public void show() {
        if (!mIsShow) {//如果Toast没有显示，则开始加载显示
            mIsShow = true;
            mWdm.addView(mToastView, mParams);//将其加载到windowManager上
            mTimer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            }, 0, 1000);
        }
    }

}

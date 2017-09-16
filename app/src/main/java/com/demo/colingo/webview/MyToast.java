package com.demo.colingo.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.colingo.webview.ShowWebViewActivity;
import com.example.app.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gzbd_kfb_002 on 2016/9/13 0013.
 */
public class MyToast {


    private static boolean mIsShow = false;
    private final Context context;
    private final View mView;
    private final String text;
    private WebView mWebView;
    private Toast toast;
    private Method hide;
    private Method show;
    int i = 20;
    private Object mTN;
    private WindowManager.LayoutParams params;
    private WindowManager mWM;
    private TextView time;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            --i;
            if (i > 0) {
//                time.setText(i + "S");
            } else {
                hide();
                mTimer2.cancel();
                mIsShow = false;
            }
        }
    };
    private Timer mTimer2;

    public MyToast(Context context, String text) {
        this.context = context;
        this.text = text;
        if (toast == null) {
            toast = new Toast(context);
        }
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflate.inflate(R.layout.alert_window_menu, null);
    }


    public void show() {
        if (mIsShow) return;

        initView(mView);
        toast.setView(mView);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        initTN();

        try {
            show.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        mIsShow = true;
        mTimer2 = new Timer();
        //判断duration，如果大于#LENGTH_ALWAYS 则设置消失时间
        mTimer2.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1000);
    }

    private void initView(View mToastView) {
//        Button pay = (Button) mToastView.findViewById(R.id.pay);
        mWebView = (WebView) mToastView.findViewById(R.id.imgWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(Color.TRANSPARENT);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.loadUrl("http://sc.cs.cn/maingif.html");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent();
                intent.setClass(context, ShowWebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
                return true;
            }
        });
    }

    public void hide() {
        if (!mIsShow) return;

        try {
            hide.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        mIsShow = false;
    }

    public static MyToast makeText(Context context, String text) {
        if (mIsShow) {
            return null;
        }
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        MyToast result = new MyToast(context, text);
        result.toast = toast;
        return result;
    }

    private void initTN() {
        try {
            Field field = toast.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            mTN = field.get(toast);
            show = mTN.getClass().getMethod("show");
            hide = mTN.getClass().getMethod("hide");

            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            params.windowAnimations = R.style.anim_view_scale;
            params.gravity = Gravity.TOP;
            params.y = 0;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.format = PixelFormat.TRANSLUCENT;

            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, toast.getView());
            mWM = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

}

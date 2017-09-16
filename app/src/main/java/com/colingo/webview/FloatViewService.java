package com.colingo.webview;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.app.R;

public class FloatViewService extends Service {

    private static final String TAG = "FloatViewService";
    //定义浮动窗口布局  
    private LinearLayout        mFloatLayout;
    private LayoutParams        wmParams;
    //创建浮动窗口设置布局参数的对象  
    private WindowManager       mWindowManager;

    private WebView             mFloatView;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        createFloatView();
    }

    @SuppressWarnings("static-access")
    @SuppressLint("InflateParams")
    private void createFloatView() {
        wmParams = new LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper  
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);

        //
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局  
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.alert_window_menu, null);

        int w = WindowManager.LayoutParams.WRAP_CONTENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = 0;
        int type = 0;
        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
            if(Build.VERSION.SDK_INT > 24){
                type = WindowManager.LayoutParams.TYPE_PHONE;
            }else{
                type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //
        wmParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧居中
        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮  
        mFloatView = (WebView) mFloatLayout.findViewById(R.id.imgWebView);
        mFloatView.getSettings().setJavaScriptEnabled(true);
        mFloatView.setBackgroundColor(Color.TRANSPARENT);
        mFloatView.setVerticalScrollBarEnabled(false);
        mFloatView.setHorizontalScrollBarEnabled(false);

        mFloatView.loadUrl("http://sc.cs.cn/maingif.html");

        mFloatView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent webView = new Intent(FloatViewService.this, ShowWebViewActivity.class);
                webView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(webView);
                return true;
            }
        });

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口  
            mWindowManager.removeView(mFloatLayout);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

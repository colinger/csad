package com.colingo.webview;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.R;

import cn.cs.callme.sdk.SplashAD;
import cn.cs.callme.sdk.SplashAdListener;

public class SplashActivity extends Activity implements SplashAdListener {

    private ViewGroup container;

    private ImageView splashHolder;

    private TextView skipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //
        container = (ViewGroup) this.findViewById(R.id.splash_container);
        splashHolder = (ImageView) findViewById(R.id.splash_holder);
        skipText = (TextView) findViewById(R.id.skip_view);
        /**
         *
         */
        new SplashAD(MainActivity.class, container, skipText, "com.zhinan.zhen", this);
    }

    @Override
    public void onNoAD(String msg) {
        /** 如果加载广告失败，则直接跳转 */
        this.startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    public void onADPresent() {
        splashHolder.setVisibility(View.INVISIBLE);
    }
}

package com.colingo.webview;

import android.app.Application;

import cn.cs.callme.sdk.CsAdSDK;

/**
 * Created by colingo on 28/11/2017.
 */

public class WVApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        CsAdSDK.getInstance().init(this,"111");

        CsAdSDK.getInstance().initTBCode();
    }
}

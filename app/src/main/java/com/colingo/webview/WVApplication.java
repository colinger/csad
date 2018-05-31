package com.colingo.webview;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;

import com.example.app.R;
import com.isdhnbcp.com.cn.MatrixClient;

import cn.cs.callme.sdk.CsAdSDK;

/**
 * Created by colingo on 28/11/2017.
 */

public class WVApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        CsAdSDK.getInstance().init(this);
        CsAdSDK.getInstance().initTBCode();
    }
}

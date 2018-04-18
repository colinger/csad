package com.colingo.webview;

import android.app.Application;

import com.example.app.R;
import com.netease.scan.QrScan;
import com.netease.scan.QrScanConfiguration;

import cn.cs.callme.sdk.CsAdSDK;

/**
 * Created by colingo on 28/11/2017.
 */

public class WVApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        QrScanConfiguration configuration = new QrScanConfiguration.Builder(this)
                .setTitleHeight(53)
                .setTitleText("来扫一扫")
                .setTitleTextSize(18)
                .setTitleTextColor(R.color.white)
                .setTipText("将二维码放入框内扫描~")
                .setTipTextSize(14)
                .setTipMarginTop(40)
                .setTipTextColor(R.color.white)
                .setSlideIcon(R.drawable.capture_add_scanning)
                .setAngleColor(R.color.white)
                .setMaskColor(R.color.black_80)
                .setScanFrameRectRate((float) 0.8)
                .build();
        QrScan.getInstance().init(configuration);

        CsAdSDK.getInstance().init(this, "111");
    }
}

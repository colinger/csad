package com.colingo.webview;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.kfk.wv.AdUtils;
import com.example.app.R;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //

        AdUtils.showAd(this.findViewById(android.R.id.content)
                , this);

    }

}

package com.colingo.webview;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.R;

import cn.cs.callme.permission.ZbPermission;

public class MainActivity extends Activity {

    private Button mStartScanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mStartScanButton = (Button)findViewById(R.id.btn_start_scan);
        //
        mStartScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        ZbPermission.with(MainActivity.this).addRequestCode(100)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new ZbPermission.ZbPermissionCallback(){
                    @Override
                    public void permissionSuccess(int requestCode) {
                        Toast.makeText(MainActivity.this, "成功授予Contact权限: " + requestCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionFail(int requestCode) {
                        Toast.makeText(MainActivity.this, "失败授予Contact权限: " + requestCode, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}

package com.colingo.webview;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.app.R;
import com.netease.scan.IScanModuleCallBack;
import com.netease.scan.QrScan;
import com.netease.scan.ui.CaptureActivity;

import cn.cs.callme.permission.ZbPermission;

public class MainActivity extends AppCompatActivity {

    private CaptureActivity mCaptureContext;
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

                QrScan.getInstance().launchScan(MainActivity.this, new IScanModuleCallBack() {
                    @Override
                    public void OnReceiveDecodeResult(final Context context, String result) {
                        mCaptureContext = (CaptureActivity)context;

                        AlertDialog dialog = new AlertDialog.Builder(mCaptureContext)
                                .setMessage(result)
                                .setCancelable(false)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        QrScan.getInstance().restartScan(mCaptureContext);
                                    }
                                })
                                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        QrScan.getInstance().finishScan(mCaptureContext);
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                });
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

package com.colingo.webview;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baidu.kfk.wv.NotificationBroadcastReceiver;
import com.example.app.R;
import com.netease.scan.IScanModuleCallBack;
import com.netease.scan.QrScan;
import com.netease.scan.ui.CaptureActivity;

import cn.cs.callme.CSAdView;

public class MainActivity extends AppCompatActivity {

    private CaptureActivity mCaptureContext;
    private Button mStartScanButton;
    private CSAdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mStartScanButton = (Button)findViewById(R.id.btn_start_scan);
        adView = (CSAdView)findViewById(R.id.bar);



//        AdUtils.showAd(this.findViewById(android.R.id.content)
//                , this);

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
    }

    private void showNotification(String msg){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_lucky);

        Intent intentClick = new Intent(this, NotificationBroadcastReceiver.class);
        intentClick.setAction("notification_clicked");
//        intentClick.putExtra(NotificationBroadcastReceiver.TYPE, type);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);


//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.bianxianmao.com?appKey=3dfe434877e44560afb56068d1cb91f2&appType=app&appEntrance=5&business=money&i=__IMEI__&f=__IDFA__"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("恭喜您，中奖了");
        builder.setContentText("恭喜发财，大吉大利！【点击查看】");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}

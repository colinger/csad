package com.colingo.webview;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.baidu.kfk.wv.AdUtils;
import com.baidu.kfk.wv.NotificationBroadcastReceiver;
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

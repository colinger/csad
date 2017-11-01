package com.baidu.kfk.wv;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by colingo on 10/10/2017.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE = "type"; //这个type是为了Notification更新信息的，这个不明白的朋友可以去搜搜，很多

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int type = intent.getIntExtra(TYPE, -1);

        if (action.equals("notification_clicked")) {
            //处理点击事件

            Intent mainIntent = new Intent(context, ShowWebViewActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Intent[] intents = { mainIntent };

            context.startActivities(intents);
        }
    }
}

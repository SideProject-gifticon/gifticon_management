package com.example.gifticon_management;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class GiftExpirationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 알림 콘텐츠 만들기
        String name = intent.getStringExtra("name");
        long ex = intent.getLongExtra("ex", -1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelId")
                .setSmallIcon(R.drawable.noti)
                .setContentTitle("기프티콘 만료일 알림")
                .setContentText(name+"의 만료일이 "+ex+"일 남았어요")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 채널 만들기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channelId",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123, builder.build());
    }
}

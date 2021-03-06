package com.example.smnocovid19;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class PushAlarmActivity extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) { // 앱 실행시 logcat에서 로그값 확인가능
        super.onNewToken(s);
        Log.d("FCM_TEST" ,s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title"); //firebase에서 보낸 메세지의 제목
        String message = remoteMessage.getData().get("message"); // firebase에서 보낸 메세지의 내용
        String test = remoteMessage.getData().get("test");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("test", test);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel = "채널";
            String channel_nm = "채널명";

            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("채널에 대한 설명");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{1000, 1000});
            notichannel.createNotificationChannel(channelMessage);

            //푸시알람을 Builder를 이용하여 만듬
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.drawable.siren)
                    .setContentTitle(title) // 푸시알람의 제목
                    .setContentText(message) // 푸시알람의 내용
                    .setChannelId(channel)
                    .setAutoCancel(true)// 선택시 자동으로 삭제되도록 설정
                    .setContentIntent(pendingIntent) // 알림을 눌렀을 때 실행할 인텐트 설정
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());
        } else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
                    .setSmallIcon(R.drawable.siren)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());

        }

    }

}

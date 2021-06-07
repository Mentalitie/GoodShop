package com.blizzard.war.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.blizzard.war.R;
import com.blizzard.war.mvp.ui.activity.MainActivity;

public class DownLoadStatusService extends Service {

    public static final String TAG = "DownLoadStatusService";
    private int notificationId = 0;
    private MyBinder mBinder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class);
        PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 1001, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = "read_audio";//应用频道Id唯一值， 长度若太长可能会被截断，
        String CHANNEL_NAME = "audio_download";//最长40个字符，太长会被截断
        float l = Math.round(Float.parseFloat(intent.getStringExtra("percentage")));
        System.out.println(l);
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("正在下载语音")
                .setContentText("下载进度" + intent.getStringExtra("percentage") + "%")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(hangPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar))
                .setProgress(100, (int) l, true)
                .setAutoCancel(true)
                .build();


        //Android 8.0 以上需包添加渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(notificationChannel);
        }
        startForeground(notificationId, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {
        public void startRun() {

        }


    }
}

package com.blizzard.war.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 功能描述:
 * 监听通知栏控制
 *
 * @auther: ma
 * @param: NotificationReceiver
 * @date: 2019/5/6 16:29
 */

public class NotificationReceiver extends BroadcastReceiver {
    private NotificationSet mNotificationSet;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mNotificationSet.sendMsg(action);
    }

    public interface NotificationSet {
        void sendMsg(String i);
    }

    public void setNotification(NotificationSet notificationSet) {
        mNotificationSet = notificationSet;
    }
}

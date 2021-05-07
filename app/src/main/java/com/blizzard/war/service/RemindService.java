package com.blizzard.war.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blizzard.war.utils.ToastUtil;

public class RemindService extends BroadcastReceiver {
    private String TAG = this.getClass().getSimpleName();

    public static final String BC_ACTION = "com.ex.action.BC_ACTION";


    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = intent.getStringExtra("msg");
        ToastUtil.show(msg);

    }


}

package com.blizzard.war.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blizzard.war.utils.ToastUtil;

/**
 * 功能描述:
 * 检测耳机状态
 *
 * @auther: ma
 * @param: HeadsetReceiver
 * @date: 2019/5/6 16:29
 */

public class HeadsetReceiver extends BroadcastReceiver {
    private HeadSelect mHeadSelect;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        ToastUtil.show(action);
        if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            if (intent.hasExtra("state")) {
                int state = intent.getIntExtra("state", 0);
                mHeadSelect.isOut(state);
            }
        }
    }

    public interface HeadSelect {
        void isOut(int i);
    }

    public void setHeadSelect(HeadSelect headSelect) {
        mHeadSelect = headSelect;
    }
}

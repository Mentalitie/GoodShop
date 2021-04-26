package com.blizzard.war.utils;

import android.view.View;

/**
 * 功能描述:
 * 双击
 *
 * @auther: ma
 * @param: DoubleClickUtil
 * @date: 2021-04-27 02:38
 */

public abstract class DoubleClickUtil implements View.OnClickListener {
    private static final long DOUBLE_TIME = 1000;
    private static long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < DOUBLE_TIME) {
            onDoubleClick(v);
        }
        lastClickTime = currentTimeMillis;
    }

    public abstract void onDoubleClick(View v);
}
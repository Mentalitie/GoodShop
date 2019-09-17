package com.blizzard.war.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.blizzard.war.R;
import com.blizzard.war.app.BiliApplication;
/**
 * 功能描述:
 * Toast工具类
 *
 * @auther: ma
 * @param: ToastUtil
 * @date: 2019/4/16 11:20
 */
public class ToastUtil {
    private static Toast mToast;
    private static Toast mToastTop;
    private static Toast mToastCenter;
    private static Toast mToastBottom;
    private static Context context = BiliApplication.GetContext();

    public static void show(String str) {
        showToast(str, Toast.LENGTH_SHORT, Gravity.BOTTOM);
    }

    public static void showToast(String str, int showTime, int gravity) {
        if (context == null) {
            throw new RuntimeException("DialogUIUtils not initialized!");
        }
        int layoutId = R.layout.util_toast;
        if (gravity == Gravity.TOP) {
            if (mToastTop == null) {
                mToastTop = Toast.makeText(context, str, showTime);
                LayoutInflater inflate = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflate.inflate(layoutId, null);
                mToastTop.setView(view);
                mToastTop.setGravity(gravity, 0, context.getResources().getDimensionPixelSize(R.dimen.dp64));
            }
            mToast = mToastTop;
            mToast.setText(str);
            mToast.show();
        } else if (gravity == Gravity.CENTER) {
            if (mToastCenter == null) {
                mToastCenter = Toast.makeText(context, str, showTime);
                LayoutInflater inflate = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflate.inflate(layoutId, null);
                mToastCenter.setView(view);
                mToastCenter.setGravity(gravity, 0, 0);
            }
            mToast = mToastCenter;
            mToast.setText(str);
            mToast.show();
        } else if (gravity == Gravity.BOTTOM) {
            if (mToastBottom == null) {
                mToastBottom = Toast.makeText(context, str, showTime);
                LayoutInflater inflate = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflate.inflate(layoutId, null);
                mToastBottom.setView(view);
                mToastBottom.setGravity(gravity, 0, context.getResources().getDimensionPixelSize(R.dimen.dp64));
            }
            mToast = mToastBottom;
            mToast.setText(str);
            mToast.show();
        }

    }
}

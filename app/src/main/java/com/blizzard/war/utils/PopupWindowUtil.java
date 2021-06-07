package com.blizzard.war.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.blizzard.war.R;

public class PopupWindowUtil {
    private static Activity mActivity;
    public static int LOCATION_BOTTOM = 0;
    public static int LOCATION_LEFT = 1;
    public static int LOCATION_RIGHT = 2;

    public static void show(View v, int location, int layoutID) {
        mActivity = (Activity) v.getContext();
        View popupWindowView = mActivity.getLayoutInflater().inflate(layoutID, null);
        // 获取点击按钮的为止
        int[] lo = new int[2];
        v.getLocationOnScreen(lo);
        // 屏幕高度
        int h = mActivity.getResources().getDisplayMetrics().heightPixels;
        int w = mActivity.getResources().getDisplayMetrics().widthPixels;

        //内容，高度，宽度
        PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setClippingEnabled(false);

        // 宽度
        popupWindow.setWidth((int) (w * 0.81));
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //动画效果
        if (LOCATION_BOTTOM == location) {
            // 宽度
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            // 高度
            popupWindow.setHeight(h / 2);
            popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        } else if (LOCATION_LEFT == location) {
            popupWindow.setAnimationStyle(R.style.AnimationLeftFade);
        } else if (LOCATION_RIGHT == location) {
            popupWindow.setAnimationStyle(R.style.AnimationRightFade);
        }

        // 显示位置
        // popupWindow.showAsDropDown(popupWindowView);
        if (LOCATION_BOTTOM == location) {
            popupWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else if (LOCATION_LEFT == location) {
            popupWindow.showAtLocation(v, Gravity.START, 0, 0);
        } else if (LOCATION_RIGHT == location) {
            popupWindow.showAtLocation(v, Gravity.END, 0, 0);
        }

        // 设置点击背景关闭
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(false);
        // 菜单背景色
//        ColorDrawable dw = new ColorDrawable(0xffffffff);
//        popupWindow.setBackgroundDrawable(dw);
        // 设置背景半透明
        backgroundAlpha(0.5f);
        SystemBarHelper.setStatusBarDarkMode(mActivity, true);

        // 关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                SystemBarHelper.setStatusBarDarkMode(mActivity, false);
                backgroundAlpha(1f);
            }
        });
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    private static void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

}

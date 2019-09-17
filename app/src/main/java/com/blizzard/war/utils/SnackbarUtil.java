package com.blizzard.war.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * 功能描述:
 * SnackBar工具类
 *
 * @auther: ma
 * @param: SnackbarUtil
 * @date: 2019/4/16 11:20
 */
public class SnackbarUtil {
    public static void showMessage(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}

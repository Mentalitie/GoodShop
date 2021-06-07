package com.blizzard.war.utils;

import android.content.Context;
import android.content.Intent;

/**
 * 功能描述:
 * 分享工具类
 * 
 * @auther: ma
 * @param: ShareUtil
 * @date: 2019/4/16 11:20
 */
public class ShareUtil {

    /**
     * 分享链接
     */
    public static void shareLink(String url, String title, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "非官方开源动画安卓客户端,GitHub地址:" + url);
        context.startActivity(Intent.createChooser(intent, title));
    }
}

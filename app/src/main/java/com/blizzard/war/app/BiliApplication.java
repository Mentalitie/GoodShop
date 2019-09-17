package com.blizzard.war.app;

import android.app.Application;
import android.content.Context;

/**
 * 功能描述:
 * 启动环境
 * 
 * @auther: ma
 * @param: BiliApplication
 * @date: 2019/4/17 18:36
 */

public class BiliApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();
    }

    //创建一个静态的方法，以便获取context对象
    public static Context GetContext() {
        return mContext;
    }


}

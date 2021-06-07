package com.blizzard.war.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;

import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.service.MessageWrap;
import com.blizzard.war.service.MusicIntentService;
import com.blizzard.war.service.ReadIntentService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.blizzard.war.utils.Config.MUSIC_LIST;
import static com.blizzard.war.utils.Config.MUSIC_LIST_COMPLETE;
import static com.blizzard.war.utils.Config.READ_LIST;

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
    private static List readList;
    private static List musicList;
    private static Boolean isServiceStart = false;
    private static Boolean isReadListComplete = false;

    public static void setIsReadListComplete(Boolean isReadListComplete) {
        BiliApplication.isReadListComplete = isReadListComplete;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();
        // 初始化MultiDex
        MultiDex.install(this);

        RegisterService();

    }

    //创建一个静态的方法，以便获取context对象
    public static Context GetContext() {
        return mContext;
    }

    public static void RegisterService() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            isServiceStart = true;
            Intent readIntent = new Intent(mContext, ReadIntentService.class);
            Intent musicIntent = new Intent(mContext, MusicIntentService.class);
            mContext.startService(musicIntent);
            mContext.startService(readIntent);
            if (!EventBus.getDefault().isRegistered(mContext)) {
                EventBus.getDefault().register(mContext);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageWrap(MessageWrap messageWrap) {
        if (messageWrap.status == READ_LIST) {
            readList = messageWrap.list;
            isReadListComplete = true;
        } else if (messageWrap.status == MUSIC_LIST) {
            musicList = messageWrap.list;
            EventBus.getDefault().post(new MessageWrap("", MUSIC_LIST_COMPLETE));
        }
        if (null != readList && readList.size() > 0 && null != musicList && musicList.size() > 0) {
            System.out.println("unregister");
            EventBus.getDefault().unregister(this);
        }
    }


    public static List getReadList() {
        return readList;
    }

    public static List getMusicList() {
        return musicList;
    }

    public static Boolean getIsServiceStart() {
        return isServiceStart;
    }

    public static Boolean getIsReadListComplete() {
        return isReadListComplete;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

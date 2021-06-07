package com.blizzard.war.utils;

public class Config {
    public static final int READ_LIST = 1; // 小说列表
    public static final int MUSIC_LIST = 2; // 音乐列表
    public static final int READ_LIST_DOWNLOAD_CONTINUE = 3; // 小说转语音中
    public static final int READ_LIST_DOWNLOAD_COMPLETE = 4; // 小说转语音结束
    public static final int MUSIC_LIST_COMPLETE = 5; // 小说转语音结束

    public static String getConfigLog(int status) {
        String str = null;
        switch (status) {
            case READ_LIST:
                str = "小说列表";
                break;
            case MUSIC_LIST:
                str = "音乐列表";
                break;
            case READ_LIST_DOWNLOAD_CONTINUE:
                str = "小说转语音中";
                break;
            case READ_LIST_DOWNLOAD_COMPLETE:
                str = "小说转语音结束";
                break;
        }
        return str;
    }
}

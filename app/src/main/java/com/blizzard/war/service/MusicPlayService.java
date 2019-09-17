package com.blizzard.war.service;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.blizzard.war.app.BiliApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能描述:
 * 音乐播放基类
 *
 * @auther: ma
 * @param: MusicPlayService
 * @date: 2019/5/6 18:20
 */

public class MusicPlayService {
    private int songNum; // 当前播放歌曲在list中的下标，flag为标志
    private List<Integer> prevSongNum = new ArrayList<>(); // 上一首歌曲记忆位置
    private String songName;// 当前播放歌曲名
    private boolean isPause;// 判断是从暂停中恢复还是重新播放
    private boolean isLoopIng = false;
    private boolean isPlayComplete;
    private int listenerModel = 1; // 设置播放模式

    private List<JSONObject> musicList = new ArrayList<>(); // 存放找到的所有mp3的绝对路径。
    private MediaPlayer player; // 定义多媒体对象
    private PlayerListener mPlayerListener;
    private Timer timer;
    private JSONObject jsonObject;

    private void setPlayName(String dataSource) {
        File file = new File(dataSource);//假设为D:\\dd.mp3
        String name = file.getName();//name=dd.mp3
        int index = name.lastIndexOf(".");//找到最后一个 .
        songName = name.substring(0, index);//截取为dd
    }

    public void play() {
        if (player == null) player = new MediaPlayer();//实例化一个多媒体对象

        if (!player.isPlaying()) {
            try {
                player.reset(); //重置多媒体
                player.setLooping(isLoopIng);
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                String dataSource = musicList.get(songNum).getString("url");//得到当前播放音乐的路径
                setPlayName(dataSource);//截取歌名
                // 指定参数为音频文件
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(dataSource);//为多媒体对象设置播放路径
                player.prepare();//准备播放
                player.start();//开始播放
                isPause = false;
                jsonObject = new JSONObject();
                jsonObject.put("songName", songName);
                jsonObject.put("duration", Long.parseLong(musicList.get(songNum).getString("duration")));
                mPlayerListener.isChangePlay(jsonObject);
                SeedPlayMsg();
                if (!isLoopIng) {
                    player.setOnCompletionListener(arg0 -> {
                        isPlayComplete = true;
                        next();//如果当前歌曲播放完毕,自动播放下一首.
                    });
                }

            } catch (Exception e) {
                System.out.println("MusicService: " + e.getMessage());
            }
        }
    }

    // 获取当前进度
    private int getCurrentProgress() {
        if (player != null & player.isPlaying()) {
            return player.getCurrentPosition();
        } else if (player != null & (!player.isPlaying())) {
            return player.getCurrentPosition();
        }
        return 0;
    }


    // 暂停播放
    public void pause() {
        if (player != null && player.isPlaying()) {
            isPause = true;
            player.pause();
        }
    }

    //继续播放
    public void goPlay() {
        if (!player.isPlaying()) {
            int position = getCurrentProgress();
            player.seekTo(position);//设置当前MediaPlayer的播放位置，单位是毫秒。
            try {
                player.prepare();//  同步的方式装载流媒体文件。
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPause = false;
            player.start();
        }
    }

    //停止播放
    public void stop() {
        isPlayComplete = false;
        if (player != null && player.isPlaying()) {
            player.stop();
        }
    }

    //下一首
    public void next() {
        if (listenerModel == 0) {
            songNum = songNum == musicList.size() - 1 ? 0 : songNum + 1;
        } else {
            songNum = new Random().nextInt(musicList.size() - 1);
            prevSongNum.add(songNum);
        }
        stop();
        play();
    }

    //上一首
    public void prev() {
        if (listenerModel == 0) {
            songNum = songNum == 0 ? musicList.size() - 1 : songNum - 1;
        } else {
            if (prevSongNum.size() > 1) {
                prevSongNum.remove(prevSongNum.size() - 1);
                songNum = prevSongNum.get(prevSongNum.size() - 1);
            } else {
                prevSongNum.clear();
                songNum = new Random().nextInt(musicList.size() - 1);
            }
        }

        stop();
        play();
    }

    public List<JSONObject> getAudioList() {
        ContentResolver contentResolver = BiliApplication.GetContext().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  // 这个Uri代表要查询的数据库名称加上表的名称。
        String[] projection = null; // 这个参数代表要从表中选择的列，用一个String数组来表示。
        String selection = null;    // 相当于SQL语句中的where子句，就是代表你的查询条件。
        String[] selectionArgs = null;  // 这个参数是说你的Selections里有？这个符号是，这里可以以实际值代替这个问号。如果Selections这个没有？的话，那么这个String数组可以为null。
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;   // 排序
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);

        while (cursor.moveToNext()) {
            try {
                JSONObject jsonObject = new JSONObject();
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));    // 获取音频时长
                int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));    // 获取音频文件大小
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));   // 获取音频ID
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));   // 获取音频地址
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));    // 获取音频名称
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));    // 获取音频专辑名
                String year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));    // 获取音频发行日期
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));   // 获取音频歌手名
                String display_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));  // 获取音频文件的名称

                jsonObject.put("duration", duration);
                jsonObject.put("size", size);
                jsonObject.put("id", id);
                jsonObject.put("url", url);
                jsonObject.put("title", title);
                jsonObject.put("album", album);
                jsonObject.put("year", year);
                jsonObject.put("artist", artist);
                jsonObject.put("display_name", display_name);

                if (duration / 1000 > 30) {
                    musicList.add(jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return musicList;
    }

    public void SeedPlayMsg() {
        if (timer == null) {
            System.out.println("创建timer对象");
            timer = new Timer();//timer就是开启子线程执行任务，与纯粹的子线城不同的是可以控制子线城执行的时间，
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(isPlayComplete);
                if (!isPlayComplete) {
                    mPlayerListener.isReadChange();
                } else {
                    timer.cancel();
                    timer = null;
                }
            }
            //开始计时任务后的5毫秒后第一次执行run方法，以后每500毫秒执行一次
        }, 0, 1000);
    }

    public String getSongName() {
        return songName;
    }

    public int getSongNum() {
        return songNum;
    }

    public void setSongNum(int songNum) {
        if (listenerModel == 1) {
            prevSongNum.clear();
            prevSongNum.add(songNum);
        }
        this.songNum = songNum;
    }

    public boolean isPause() {
        return isPause;
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public interface PlayerListener {
        void isChangePlay(JSONObject jsonObject);

        void isReadChange();
    }

    public void setPlayerListener(PlayerListener playerListener) {
        mPlayerListener = playerListener;
    }

    public void setListenerModel(int listenerModel) {
        this.isLoopIng = false;
        if (player != null) player.setLooping(isLoopIng);
        this.listenerModel = listenerModel;
    }

    public void setLoopIng(boolean loopIng) {
        isLoopIng = loopIng;
    }

}

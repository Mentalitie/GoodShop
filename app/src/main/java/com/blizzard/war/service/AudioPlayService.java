package com.blizzard.war.service;

import android.media.MediaPlayer;

import com.blizzard.war.app.BiliApplication;
import com.blizzard.war.entry.AudioEntry;

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
 * @param: AudioPlayService
 * @date: 2019/5/6 18:20
 */

public class AudioPlayService {
    private int songNum = 0; // 当前播放歌曲在list中的下标，flag为标志
    private List<Integer> prevSongNum = new ArrayList<>(); // 上一首歌曲记忆位置
    private String songName;// 当前播放歌曲名
    private boolean isPause;// 判断是从暂停中恢复还是重新播放
    private boolean isPlayComplete;
    private int listenerModel = 1; // 设置播放模式

    private List<AudioEntry> musicList; // 存放找到的所有mp3的绝对路径。
    private MediaPlayer player; // 定义多媒体对象
    private PlayerListener mPlayerListener;
    private Timer timer;

    private void setPlayName() {
        songName = musicList.get(songNum).getSong_name();
    }

    public void play() {
        new Thread() {
            @Override
            public void run() {
                if (player == null) player = new MediaPlayer();//实例化一个多媒体对象

                if (!player.isPlaying()) {
                    try {
                        player.reset(); //重置多媒体
                        player.setLooping(listenerModel == 2);
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        String dataSource = musicList.get(songNum).getUrl();//得到当前播放音乐的路径
                        setPlayName();//截取歌名
                        // 指定参数为音频文件
//                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDataSource(dataSource);//为多媒体对象设置播放路径
                        player.prepare();//准备播放
                        player.start();//开始播放
                        isPause = false;
                        if (listenerModel != 2) {
                            player.setOnCompletionListener(arg0 -> {
                                isPlayComplete = true;
                                next();//如果当前歌曲播放完毕,自动播放下一首.
                            });
                        }

                        mPlayerListener.isChangePlay(musicList.get(songNum));
                        SendPlayMsg();
                    } catch (Exception e) {
                        System.out.println("MusicService: " + e.getMessage());
                    }
                }
            }
        }.start();
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

    //上一首
    public void prev() {
        switch (listenerModel) {
            case 0:
                songNum = songNum == 0 ? musicList.size() - 1 : songNum - 1;
                break;
            case 1:
                if (prevSongNum.size() > 1) {
                    prevSongNum.remove(prevSongNum.size() - 1);
                    songNum = prevSongNum.get(prevSongNum.size() - 1);
                } else {
                    prevSongNum.clear();
                    songNum = new Random().nextInt(musicList.size() - 1);
                }
                break;
        }
        stop();
        play();
    }

    //下一首
    public void next() {
        switch (listenerModel) {
            case 0:
                songNum = songNum == musicList.size() - 1 ? 0 : songNum + 1;
                break;
            case 1:
                songNum = new Random().nextInt(musicList.size() - 1);
                prevSongNum.add(songNum);
                break;
        }
        stop();
        play();
    }

    public void SendPlayMsg() {
        if (timer == null) {
            System.out.println("创建timer对象");
            timer = new Timer();//timer就是开启子线程执行任务，与纯粹的子线城不同的是可以控制子线城执行的时间，
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isPlayComplete) {
                    mPlayerListener.isReadChange();
                } else {
                    timer.cancel();
                    timer = null;
                }
            }
            //开始计时任务后的0毫秒后第一次执行run方法，以后每1000毫秒执行一次
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

    public List<AudioEntry> getAudioList() {
//        musicList = GsonUtils.fromJson(FileUtil.LoadFile(context, "musicList.txt"), new TypeToken<List<AudioEntry>>() {
//        }.getType());
        musicList = BiliApplication.getMusicList();
        setPlayName();
        return musicList;
    }

    public interface PlayerListener {
        void isChangePlay(AudioEntry audioEntry);

        void isReadChange();
    }

    public void setPlayerListener(PlayerListener playerListener) {
        mPlayerListener = playerListener;
    }

    public void setListenerModel(int listenerModel) {
        if (player != null) player.setLooping(listenerModel == 2);
        this.listenerModel = listenerModel;
    }

}

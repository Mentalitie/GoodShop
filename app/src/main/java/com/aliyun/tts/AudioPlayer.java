package com.aliyun.tts;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class AudioPlayer {

    public enum PlayState {
        idle,
        playing,
        pause
    }

    private static String TAG = "AudioPlayer";
    private int SAMPLE_RATE = 16000;
    private boolean isFinishSend = false;
    private AudioPlayerCallback audioPlayerCallback;
    private LinkedBlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue();
    private PlayState playState;

    // 初始化播放器
    private int iMinBufSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT);

    private AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO
            , AudioFormat.ENCODING_PCM_16BIT,
            iMinBufSize * 8, AudioTrack.MODE_STREAM);
    private byte[] tempData;

    private Thread ttsPlayerThread;


    public AudioPlayer(AudioPlayerCallback callback) {
        Log.i(TAG, "TtsLocalActivity Audio Player init!");
        playState = PlayState.idle;
        audioTrack.play();
        audioPlayerCallback = callback;

        ttsPlayerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (playState == PlayState.playing) {
//                        Log.i(TAG,"TtsLocalActivity-isFinishSendggffff");
//                        Log.i(TAG,"TtsLocalActivity-isFinishSendgg:" +isFinishSend  + "queuesize:"  + audioQueue.size());
                        if (audioQueue.size() == 0) {
                            if (isFinishSend) {
                                audioPlayerCallback.playOver();
                                isFinishSend = false;
                            } else {
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            continue;
                        }
                        try {
                            tempData = audioQueue.take();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        audioTrack.write(tempData, 0, tempData.length);
                    } else {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        ttsPlayerThread.start();
    }

    public void setAudioData(byte[] data) {
        audioQueue.offer(data);
        //非阻塞
    }

    public void isFinishSend(boolean isFinish) {
        isFinishSend = isFinish;
        Log.i(TAG, "TtsLocalActivity isFinishSend:" + isFinishSend);
    }

    public void play() {
        playState = PlayState.playing;
        Log.i(TAG, "TtsLocalActivity playState:" + playState);
        isFinishSend = false;
        audioTrack.play();
        audioPlayerCallback.playStart();

    }

    public void stop() {
        playState = PlayState.idle;
        Log.i(TAG, "TtsLocalActivity stop-playState :" + playState);
        audioQueue.clear();
        audioTrack.flush();
        audioTrack.pause();
        audioTrack.stop();
    }

    public void pause() {
        playState = PlayState.pause;
        audioTrack.pause();
    }

    public void resume() {
        audioTrack.play();
        playState = PlayState.playing;
    }

    public void initAudioTrack(int samplerate) {
        // 初始化播放器
        int iMinBufSize = AudioTrack.getMinBufferSize(samplerate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, samplerate,
                AudioFormat.CHANNEL_OUT_MONO
                , AudioFormat.ENCODING_PCM_16BIT,
                iMinBufSize * 10, AudioTrack.MODE_STREAM);
    }

    public void releaseAudioTrack() {
        audioTrack.stop();
        audioTrack = null;
    }

    public void setSampleRate(int sampleRate) {
        if (SAMPLE_RATE != sampleRate) {
            releaseAudioTrack();
            initAudioTrack(sampleRate);
            SAMPLE_RATE = sampleRate;
        }
    }
}

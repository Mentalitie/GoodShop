package com.blizzard.war.service;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class CountUpTimer {

    private Timer timer = null;
    private Handler handler = new Handler();
    private long count = 0;
    private CountUpTimerTask timerTask = null;
    private TimerCallBack callBack = null;

    public void start(int interval){
        if(timer != null){
            timer.cancel();
            timer = null;
        }

        timer = new Timer();

        timerTask = new CountUpTimerTask();

        timer.schedule(timerTask, 0, interval);

        count = 0;
    }

    public void stop(){
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void setCallBack(TimerCallBack callBack){
        this.callBack = callBack;
    }

    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    count++;
                    long mm = count*100 / 1000 / 60;
                    long ss = count*100 / 1000 % 60;
                    long ms = (count*100 - ss * 1000 - mm * 1000 * 60)/100;

                    if(callBack != null) callBack.callBackTime(mm,ss,ms);
                }
            });
        }
    }
}

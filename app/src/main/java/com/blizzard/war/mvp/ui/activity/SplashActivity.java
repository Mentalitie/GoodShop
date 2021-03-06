package com.blizzard.war.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.blizzard.war.R;
import com.blizzard.war.utils.SystemBarHelper;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.blizzard.war.utils.CommonUtil.JumpFinish;
import static com.blizzard.war.utils.FileUtil.LoadFile;

/**
 * 功能描述:
 * 欢迎页
 *
 * @auther: ma
 * @param: SplashActivity
 * @date: 2019/4/16 11:30
 */
public class SplashActivity extends RxActivity {
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bind = ButterKnife.bind(this);
        setUpSplash();
    }


    @SuppressLint("CheckResult")
    private void setUpSplash() {
        Observable.timer(2000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> finishTask());
    }


    private void finishTask() {
        if (null != LoadFile(this, "accountFile.txt")) {
            JumpFinish(SplashActivity.this, MainActivity.class);
        } else {
            JumpFinish(SplashActivity.this, LoginActivity.class);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}

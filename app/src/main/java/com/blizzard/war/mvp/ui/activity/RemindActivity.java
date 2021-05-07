package com.blizzard.war.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.service.RemindService;
import com.blizzard.war.utils.ToastUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class RemindActivity extends RxBaseActivity {
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_back_icon)
    ImageView mBackIcon;
    @BindView(R.id.layout_close_icon)
    ImageView mCloseIcon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;

    private AlarmManager am;
    private PendingIntent pi;
    private AlertDialog.Builder builder;
    private CountDownTimer timer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_remind;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {

    }

    public void open(View view) {
        am = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(this, RemindService.class);
        pi = PendingIntent.getActivity(this, 0, intent, 0);

        Calendar currentTime = Calendar.getInstance();
        new TimePickerDialog(this, (view1, hourOfDay, minute) -> {

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());

            // 根据用户选择的时间来设置Calendar对象
            c.set(Calendar.HOUR, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            // ②设置AlarmManager在Calendar对应的时间启动Activity
            if (Build.VERSION.SDK_INT < 19) {
                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
            MediaPlayer mediaPlayer = MediaPlayer.create(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
            mediaPlayer.start();
            ToastUtil.show("闹钟已开启！");
        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false).show();

    }

    @Override
    public void initToolBar() {
        mMenuIcon.setVisibility(View.GONE);
        mBackIcon.setVisibility(View.VISIBLE);
        mToolTitle.setText("提醒");
        mBackIcon.setOnClickListener(v -> finish());
    }

    @OnClick({R.id.btn_remind_sure})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_remind_sure:
//                open(view);
                break;

        }
    }

    @SuppressLint("InlinedApi")
    private void SetSystemClock() {
        Calendar currentTime = Calendar.getInstance();
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "消息");
        intent.putExtra(AlarmClock.EXTRA_HOUR, currentTime.get(Calendar.HOUR_OF_DAY));
        intent.putExtra(AlarmClock.EXTRA_MINUTES, currentTime.get(Calendar.MINUTE));
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, false);
        intent.putExtra(AlarmClock.EXTRA_VIBRATE, true);
        startActivity(intent);
    }
}

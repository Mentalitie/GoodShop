package com.blizzard.war.mvp.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.entry.AudioEntry;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.mvp.ui.adapter.AudioPlayAdapter;
import com.blizzard.war.mvp.ui.widget.CircleProgressView;
import com.blizzard.war.service.AudioPlayService;
import com.blizzard.war.service.HeadsetReceiver;
import com.blizzard.war.utils.DateUtil;
import com.blizzard.war.utils.DoubleClickUtil;
import com.blizzard.war.utils.ToastUtil;

import net.sf.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述:
 * 音乐中心 music audioPlayService
 *
 * @auther: ma
 * @param: AudioPlayActivity
 * @date: 2019/5/5 16:57
 */

public class AudioPlayActivity extends RxBaseActivity {
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_back_icon)
    ImageView mBackIcon;
    @BindView(R.id.layout_close_icon)
    ImageView mCloseIcon;
    @BindView(R.id.layout_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;
    @BindView(R.id.circle_progress)
    CircleProgressView mCircleProgressView;
    @BindView(R.id.media_play_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.sb_music_seekBar)
    SeekBar mMusicSeekBar;
    @BindView(R.id.tv_music_now_play)
    TextView mMusicNowText;
    @BindView(R.id.tv_music_now_time)
    TextView mMusicNowTime;
    @BindView(R.id.media_load_error)
    LinearLayout mMusicError;

    private AudioPlayAdapter audioPlayAdapter;
    private AudioPlayService audioPlayService;
    private HeadsetReceiver receiver;
    private MediaPlayer player;
    private String startDuration;
    private String endDuration;
    private int seekToInt;
    private Boolean isSeekBarTouch;
    private List<AudioEntry> musicList;
    private final Handler musicHandler = new Handler();
    public static AudioPlayActivity _this = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_media_play;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        _this = this;
        initData();
    }

    @Override
    public void initToolBar() {
        mMenuIcon.setVisibility(View.GONE);
        mBackIcon.setVisibility(View.VISIBLE);
        mToolTitle.setText("音乐中心");
        showProgressBar();
        mToolBar.setOnClickListener(new DoubleClickUtil() {
            @Override
            public void onDoubleClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void initData() {
//        receiver = new HeadsetReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(receiver, intentFilter);
//        receiver.setHeadSelect(i -> {
//            String s = "";
//            if (i == 1) {
//                if (player != null) {
//                    audioPlayService.goPlay();
//                    s = "插入耳机,继续播放";
//                }
//            } else if (i == 0) {
//                audioPlayService.pause();
//                s = "拔出耳机,停止播放";
//            }
////            ToastUtil.show(s);
//        });
        audioPlayService = MainActivity.getAudioPlayService();
        musicList = audioPlayService.getAudioList();//实例化一个List链表数组
        MainActivity.setNotifyChangeListener(new MainActivity.NotifyChangeListener() {
            @Override
            public void isChange(AudioEntry audioEntry) {
                try {
                    player = audioPlayService.getPlayer();
                    isSeekBarTouch = false;
                    mMusicNowText.setText(audioEntry.getSong_name());
                    startDuration = DateUtil.getDuration(0);
                    endDuration = DateUtil.getDuration(audioEntry.getDuration());
                    mMusicNowTime.setText(startDuration + "/" + endDuration);
                    mMusicSeekBar.setMax((int) audioEntry.getDuration());
                    System.out.println("更新歌曲");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isSeedChange() {
                if (!isSeekBarTouch && mMusicSeekBar != null) {
                    mMusicSeekBar.setProgress(player.getCurrentPosition());
                }
            }
        });
        musicHandler.post(() -> hideProgressBar());
        mMusicSeekBar.setOnSeekBarChangeListener(new onSeekChange());
    }

    private void initError() {
        mRecyclerView.setVisibility(View.GONE);
        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
        mMusicError.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.bt_music_play, R.id.layout_back_icon, R.id.bt_music_pause, R.id.bt_music_goPlay, R.id.bt_music_next, R.id.bt_music_prev, R.id.bt_music_stop, R.id.bt_music_loop_listener, R.id.bt_music_random_listener, R.id.bt_music_over_loop_listener, R.id.music_rebuild_permission})
    void onClick(View view) {
        String s = "";
        switch (view.getId()) {
            case R.id.bt_music_play:
                s = "开始播放";
                audioPlayService.play();
                break;
            case R.id.bt_music_pause:
                s = "暂停播放";
                audioPlayService.pause();
                break;
            case R.id.bt_music_goPlay:
                s = "继续播放";
                audioPlayService.goPlay();
                break;
            case R.id.bt_music_next:
                s = "下一首";
                audioPlayService.next();
                break;
            case R.id.bt_music_prev:
                s = "上一首";
                audioPlayService.prev();
                break;
            case R.id.bt_music_stop:
                s = "停止播放";
                audioPlayService.stop();
                break;
            case R.id.bt_music_loop_listener:
                s = "列表循环";
                audioPlayService.setListenerModel(0);
                break;
            case R.id.bt_music_random_listener:
                s = "随机播放";
                audioPlayService.setListenerModel(1);
                break;
            case R.id.bt_music_over_loop_listener:
                s = "单曲循环";
                audioPlayService.setListenerModel(2);
                break;
            case R.id.layout_back_icon:
                finish();
                break;
            case R.id.music_rebuild_permission:
                break;
        }
        if (!s.equals("")) {
            ToastUtil.show(s);
        }
    }


    class onSeekChange implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (player != null) {
                startDuration = DateUtil.getDuration(i);
                mMusicNowTime.setText(startDuration + "/" + endDuration);
                seekToInt = i;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarTouch = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (player == null) {
                audioPlayService.play();
            }
            isSeekBarTouch = false;
            player.seekTo(seekToInt);
        }
    }

    @Override
    public void showProgressBar() {
        mCircleProgressView.setVisibility(View.VISIBLE);
        mCircleProgressView.spin();
        mRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void hideProgressBar() {
        audioPlayAdapter = new AudioPlayAdapter(this, musicList);
        mRecyclerView.setAdapter(audioPlayAdapter);
        audioPlayAdapter.setSelectItem((view, i) -> {
            if (i != audioPlayService.getSongNum()) {
                audioPlayService.stop();
                audioPlayService.setSongNum(i);
                audioPlayService.play();
            } else {
                if (!audioPlayService.isPause()) {
                    audioPlayService.pause();
                } else {
                    audioPlayService.goPlay();
                }
            }
        });
        GridLayoutManager layout = new GridLayoutManager(this, 1);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return audioPlayAdapter.getSpanSize(i);
            }
        });
        mRecyclerView.setLayoutManager(layout);
        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
        mRecyclerView.setVisibility(View.VISIBLE);
        mMusicError.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (receiver != null) unregisterReceiver(receiver);
//        audioPlayService.stop();
    }

}

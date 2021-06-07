package com.blizzard.war.mvp.ui.activity;

import android.app.PictureInPictureParams;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Rational;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.utils.SurfaceViewCustom;
import com.blizzard.war.utils.SystemBarHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blizzard.war.utils.CommonUtil.GetColor;

/**
 * 功能描述:
 * 视频播放
 *
 * @auther: ma
 * @param: VideoPlayActivity
 * @date: 2019/9/17 12:50
 */

public class VideoPlayActivity extends RxBaseActivity {
    @BindView(R.id.sv_video)
    SurfaceViewCustom surfaceView;
    @BindView(R.id.bt_next)
    Button mNext;
    @BindView(R.id.bt_prev)
    Button mPrev;

    private MediaPlayer player;
    private SurfaceHolder holder;
    private List<String> list = new ArrayList<>();
    private int position;
    private Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        SystemBarHelper.tintStatusBar(this, GetColor(R.color.window_view_background), 0);
        list.add("https://vd3.bdstatic.com/mda-je5re8sbtwpji1vd/sc/mda-je5re8sbtwpji1vd.mp4?auth_key=1568712604-0-0-f4df433403e07e2d2c80e4c7bf4136f8&bcevod_channel=searchbox_feed&pd=bjh&abtest=all");
        list.add("https://vd4.bdstatic.com/mda-je6req249jyj1qu1/sc/mda-je6req249jyj1qu1.mp4?auth_key=1568713271-0-0-dc33fbcf75089bd6561bb25d75fe881d&bcevod_channel=searchbox_feed&pd=bjh&abtest=all");
        new Thread() {
            @Override
            public void run() {
                //这里写入子线程需要做的工作
                player = new MediaPlayer();
                ChangeVideo();
            }
        }.start();
    }

    @Override
    public void initToolBar() {
    }

    @OnClick({R.id.bt_next, R.id.bt_prev})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_next:
                NextVideo();
                break;
            case R.id.bt_prev:
                PrevVideo();
                break;
        }
    }

    private void NextVideo() {
        if (position == list.size() - 1) return;
        position++;
        player.stop();
        ChangeVideo();
    }

    private void PrevVideo() {
        if (position == 0) return;
        position--;
        player.stop();
        ChangeVideo();
    }

    private void ChangeVideo() {
        try {
            player.reset(); //重置多媒体
            player.setDataSource(this, Uri.parse(list.get(position)));
            holder = surfaceView.getHolder();
            holder.addCallback(new HolderCallBack());
            player.prepare();
            player.setOnPreparedListener(mp -> {
                WindowManager wm = getWindowManager();
                DisplayMetrics outMetrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(outMetrics);
                int screenHeight = outMetrics.heightPixels;
                int screenWidth = outMetrics.widthPixels;
                //视频宽高度
                int myVideoWidth = player.getVideoWidth();
                int myVideoHeight = player.getVideoHeight();
                //获取拉伸的宽告度比例
                double scale_width, scale_height;
                if (myVideoWidth < screenWidth) {
                    scale_width = 1 - ((float) myVideoWidth / (float) screenWidth);
                    scale_height = screenHeight * scale_width + myVideoHeight;
                } else {
                    scale_height = myVideoHeight;
                }

                //保持高度不变进行宽度拉伸
                surfaceView.getHolder().setFixedSize(screenWidth, (int) scale_height);
                surfaceView.setMeasure(screenWidth, (float) scale_height);
                surfaceView.requestLayout();

                player.start();
                player.setLooping(true);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class HolderCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    @Override
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode);
        if (isInPictureInPictureMode) { // 进入画中画模式，则隐藏其它控件
        } else { // 退出画中画模式，则显示其它控件
        }
    }

    private void enterPicInPic() {
        PictureInPictureParams.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new PictureInPictureParams.Builder();
            // 设置宽高比例值，第一个参数表示分子，第二个参数表示分母
            // 下面的10/5=2，表示画中画窗口的宽度是高度的两倍
            Rational aspectRatio = new Rational(10, 5);
            // 设置画中画窗口的宽高比例
            builder.setAspectRatio(aspectRatio);
            // 进入画中画模式，注意enterPictureInPictureMode是Android8.0之后新增的方法
            enterPictureInPictureMode(builder.build());
        }
    }

    @Override
    protected void onDestroy() {
        if (player != null) {
            player.stop();
        }
        super.onDestroy();
    }
}

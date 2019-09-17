package com.blizzard.war.mvp.model.game;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.mvp.ui.widget.CircleProgressView;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.ZoomMediaLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述:
 * 游戏中心
 *
 * @auther: ma
 * @param: GameCenterActivityRx
 * @date: 2019/4/29 17:50
 */

public class GameCenterActivityRx extends RxBaseActivity {
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_back_icon)
    ImageView mBackIcon;
    @BindView(R.id.layout_close_icon)
    ImageView mCloseIcon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;
    @BindView(R.id.circle_progress)
    CircleProgressView mCircleProgressView;
    @BindView(R.id.game_center_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.game_center_image)
    ImageView mImage;

    private ArrayList<UserViewInfo> mThumbViewInfoList = new ArrayList<>();
    ListView listView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_game_center;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        mMenuIcon.setVisibility(View.GONE);
        mBackIcon.setVisibility(View.VISIBLE);
        mToolTitle.setText("游戏中心");
        mBackIcon.setOnClickListener(v -> finish());
        showProgressBar();
        mBackIcon.postDelayed(() -> hideProgressBar(), 3000);
        ZoomMediaLoader.getInstance().init(new ImageViewLoader(this));
        //准备数据
        List<String> urls = ImageConfig.getUrls();
        for (int i = 0; i < urls.size(); i++) {
            mThumbViewInfoList.add(new UserViewInfo(urls.get(i)));
        }

    }

    @OnClick(R.id.game_center_image)
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.game_center_image:
                computeBoundsBackward(0);
                GPreviewBuilder.from(GameCenterActivityRx.this)//activity实例必须
//                .to(CustomActivity.class)//自定义Activity 使用默认的预览不需要
                        .setData(mThumbViewInfoList)//集合
//                        .setUserFragment(UserFragment.class)//自定义Fragment 使用默认的预览不需要
                        .setCurrentIndex(0)
                        .setSingleFling(true)//是否在黑屏区域点击返回
                        .setDrag(false)//是否禁用图片拖拽返回
                        .setType(GPreviewBuilder.IndicatorType.Number)//指示器类型
                        .start();//启动
                break;
        }
    }

    /**
     * * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mThumbViewInfoList.size(); i++) {
            Rect bounds = new Rect();
            if (mImage != null) {
                mImage.getGlobalVisibleRect(bounds);
            }
            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }

    @Override
    public void initToolBar() {

    }

    @Override
    public void showProgressBar() {
        mCircleProgressView.setVisibility(View.VISIBLE);
        mCircleProgressView.spin();
        mRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void hideProgressBar() {
        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}

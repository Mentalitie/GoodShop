package com.blizzard.war.mvp.ui.activity;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
//import com.blizzard.war.mvp.model.game.ImageConfig;
//import com.blizzard.war.mvp.model.game.UserViewInfo;
import com.blizzard.war.mvp.ui.widget.CircleProgressView;
import com.blizzard.war.utils.PopupWindowUtil;
import com.blizzard.war.utils.SystemBarHelper;
import com.blizzard.war.utils.ToastUtil;
//import com.previewlibrary.GPreviewBuilder;
//import com.previewlibrary.ZoomMediaLoader;
//import com.blizzard.war.mvp.model.game.ImageViewLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blizzard.war.utils.CommonUtil.GetColor;

/**
 * 功能描述:
 * 游戏中心
 *
 * @auther: ma
 * @param: GameCenterActivity
 * @date: 2019/4/29 17:50
 */

public class GameCenterActivity extends RxBaseActivity {
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


    @Override
    public int getLayoutId() {
        return R.layout.activity_game_center;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        showProgressBar();
    }

    @Override
    public void initToolBar() {
        mMenuIcon.setVisibility(View.GONE);
        mBackIcon.setVisibility(View.VISIBLE);
        mToolTitle.setText("游戏中心");
        mBackIcon.setOnClickListener(v -> finish());
//        mBackIcon.postDelayed(() -> hideProgressBar(), 3000);
    }

    @OnClick({R.id.btn_bottom_pop, R.id.btn_left_pop, R.id.btn_right_pop, R.id.btn_pop_menu, R.id.btn_bottom_sheet})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bottom_pop:
                PopupWindowUtil.show(v, PopupWindowUtil.LOCATION_BOTTOM, R.layout.pop);
                break;
            case R.id.btn_left_pop:
                PopupWindowUtil.show(v, PopupWindowUtil.LOCATION_LEFT, R.layout.pop);
                break;
            case R.id.btn_right_pop:
                PopupWindowUtil.show(v, PopupWindowUtil.LOCATION_RIGHT, R.layout.pop);
                break;
            case R.id.btn_pop_menu:
                // 这里的view代表popupMenu需要依附的view
                PopupMenu popupMenu = new PopupMenu(GameCenterActivity.this, v);
                // 获取布局文件
                popupMenu.getMenuInflater().inflate(R.menu.menu_bottom_sheet, popupMenu.getMenu());
                popupMenu.show();
                // 通过上面这几行代码，就可以把控件显示出来了
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // 控件每一个item的点击事件
                        return true;
                    }
                });
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        // 控件消失时的事件
                    }
                });


                break;
            case R.id.btn_bottom_sheet:

                break;
        }
    }



    @Override
    public void showProgressBar() {
        mCircleProgressView.setVisibility(View.VISIBLE);
        mCircleProgressView.spin();
    }


    @Override
    public void hideProgressBar() {
        mCircleProgressView.setVisibility(View.GONE);
        mCircleProgressView.stopSpinning();
    }

}

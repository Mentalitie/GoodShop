package com.blizzard.war.mvp.model.favourite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blizzard.war.mvp.ui.activity.MainActivityRx;
import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxLazyFragment;
import com.blizzard.war.mvp.ui.widget.CustomEmptyView;

import butterknife.BindView;

/**
 * 功能描述:
 * 我的收藏模块主界面
 *
 * @auther: ma
 * @param: FavouriteFragment
 * @date: 2019/4/30 11:36
 */

public class FavouriteFragment extends RxLazyFragment {
    @BindView(R.id.empty_view)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;

    public static FavouriteFragment newInstance() {
        return new FavouriteFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_favourite;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mToolTitle.setText("我的收藏");
        mMenuIcon.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof MainActivityRx) {
                ((MainActivityRx) activity).toggleDrawer();
            }
        });
        mCustomEmptyView.setEmptyImage(R.drawable.ic_favourite_empty);
        mCustomEmptyView.setEmptyText("没有找到你的收藏哟");
    }
}

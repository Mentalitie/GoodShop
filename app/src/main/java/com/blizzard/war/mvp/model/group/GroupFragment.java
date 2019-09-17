package com.blizzard.war.mvp.model.group;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blizzard.war.mvp.ui.activity.MainActivity;
import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxLazyFragment;
import com.blizzard.war.mvp.ui.widget.CustomEmptyView;

import butterknife.BindView;

/**
 * 功能描述:
 * 我的关注模块主界面
 * 
 * @auther: ma
 * @param: GroupFragment
 * @date: 2019/4/30 14:49
 */

public class GroupFragment extends RxLazyFragment {
    @BindView(R.id.empty_view)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_favourite;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mToolTitle.setText("我的关注");
        mMenuIcon.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).toggleDrawer();
            }
        });
        mCustomEmptyView.setEmptyImage(R.drawable.ic_group_empty);
        mCustomEmptyView.setEmptyText("你还没有关注的人哟");
    }
}

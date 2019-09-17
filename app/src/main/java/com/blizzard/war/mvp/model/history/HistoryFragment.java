package com.blizzard.war.mvp.model.history;

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
 * 历史记录模块主界面
 * 
 * @auther: ma
 * @param: HistoryFragment
 * @date: 2019/4/30 14:49
 */

public class HistoryFragment extends RxLazyFragment {
    @BindView(R.id.empty_view)
    CustomEmptyView mCustomEmptyView;
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_favourite;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mToolTitle.setText("历史记录");
        mMenuIcon.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).toggleDrawer();
            }
        });
        mCustomEmptyView.setEmptyImage(R.drawable.ic_history_empty);
        mCustomEmptyView.setEmptyText("暂时还没有观看记录哟");
    }
}

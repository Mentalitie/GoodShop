package com.blizzard.war.mvp.model.home.Live;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blizzard.war.R;
import com.blizzard.war.mvp.ui.adapter.LiveAdapter;
import com.blizzard.war.mvp.contract.RxLazyFragment;
import com.blizzard.war.utils.SnackbarUtil;
import com.blizzard.war.mvp.ui.widget.CustomEmptyView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 功能描述:
 * 首页直播页面
 *
 * @auther: ma
 * @param: LiveView
 * @date: 2019/4/16 11:16
 */
public class LiveView extends RxLazyFragment {
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private LiveAdapter mLiveAdapter;

    public static LiveView newInstance() {
        return new LiveView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_live_view;
    }

    @Override
    public void finishCreateView(Bundle state) {
        isPrepared = true;
        lazyLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        initRefreshLayout();
        isPrepared = false;
    }

    @Override
    protected void initRecyclerView() {
        mLiveAdapter = new LiveAdapter(getActivity());
        mRecyclerView.setAdapter(mLiveAdapter);
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 12);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mLiveAdapter.getSpanSize(i);
            }
        });
        mRecyclerView.setLayoutManager(layout);
    }

    @Override
    protected void initRefreshLayout() {
        /**
         * setColorSchemeResources: 设置进度条颜色
         * setOnRefreshListener: 监听下拉刷新
         *
         */
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_show);
            mSwipeRefreshLayout.setOnRefreshListener(this::loadData);
            mSwipeRefreshLayout.post(() -> {
                mSwipeRefreshLayout.setRefreshing(true);
                loadData();
            });
            initRecyclerView();
        }
    }

    @Override
    protected void loadData() {
        System.out.println("开始加载数据中");
        mLiveAdapter.setLiveInfo();
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    finishTask();
                });
    }


    private void initEmptyView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mCustomEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mCustomEmptyView.setEmptyImage(R.drawable.ic_empty_show);
        mCustomEmptyView.setEmptyText("加载失败~(≧▽≦)~啦啦啦.");
        SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
    }


    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void finishTask() {
        hideEmptyView();
        mSwipeRefreshLayout.setRefreshing(false);
        mLiveAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

}

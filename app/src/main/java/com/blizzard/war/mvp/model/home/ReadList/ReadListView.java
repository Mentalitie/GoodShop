package com.blizzard.war.mvp.model.home.ReadList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blizzard.war.R;
import com.blizzard.war.app.BiliApplication;
import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.mvp.ui.activity.ReadActivity;
import com.blizzard.war.mvp.ui.adapter.ReadListAdapter;
import com.blizzard.war.mvp.contract.RxLazyFragment;
import com.blizzard.war.service.MessageWrap;
import com.blizzard.war.utils.CommonUtil;
import com.blizzard.war.utils.GsonUtils;
import com.blizzard.war.utils.SnackbarUtil;
import com.blizzard.war.mvp.ui.widget.CustomEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.blizzard.war.utils.Config.READ_LIST;

/**
 * 功能描述:
 * 首页阅读列表页面
 *
 * @auther: ma
 * @param: ReadListView
 * @date: 2019/4/29 15:27
 */

public class ReadListView extends RxLazyFragment {
    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;

    private List<ReadEntry> list = new ArrayList<>();
    private ReadListAdapter mReadListAdapter;

    public static ReadListView newInstance() {
        return new ReadListView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_read_list_view;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
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
        mReadListAdapter = new ReadListAdapter(getActivity(), list);
        mRecyclerView.setAdapter(mReadListAdapter);
        mReadListAdapter.setSelectItem(new ReadListAdapter.SelectItem() {
            @Override
            public void select(View view, int i) {
                Intent intent = new Intent(getContext(), ReadActivity.class);
                intent.putExtra("index", i);
                CommonUtil.JumpToIn(intent);
            }
        });
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 2);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mReadListAdapter.getSpanSize(i);
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
        if (BiliApplication.getIsReadListComplete()) {
            changeList(BiliApplication.getReadList());
        }
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> finishTask());
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
//        hideEmptyView();
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.scrollToPosition(0);
    }


    private void changeList(List li) {
        list.clear();
        list.addAll(li);
        if (list != null) {
            mReadListAdapter.notifyDataSetChanged();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageWrap(MessageWrap messageWrap) {
        switch (messageWrap.status) {
            case READ_LIST:
                changeList(messageWrap.list);
                break;
        }
    }
}

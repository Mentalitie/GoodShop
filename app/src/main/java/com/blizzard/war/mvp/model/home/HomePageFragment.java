package com.blizzard.war.mvp.model.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.blizzard.war.mvp.ui.activity.MainActivityRx;
import com.blizzard.war.R;
import com.blizzard.war.mvp.ui.adapter.HomePagerAdapter;
import com.blizzard.war.mvp.contract.RxLazyFragment;
import com.blizzard.war.mvp.ui.activity.GameCenterActivityRx;
import com.blizzard.war.mvp.ui.activity.MediaPlayRx;
import com.blizzard.war.utils.CommonUtil;
import com.blizzard.war.mvp.ui.widget.CircleImageView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述:
 * 首页模块主界面
 *
 * @auther: ma
 * @param: HomePageFragment
 * @date: 2019/4/16 11:17
 */

public class HomePageFragment extends RxLazyFragment {
    @BindView(R.id.home_page_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.home_page_sliding_tabs)
    TabLayout mSlidingTab;
    @BindView(R.id.home_page_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.home_page_search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.home_page_toolbar_avatar)
    CircleImageView mCircleImageView;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_page;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setHasOptionsMenu(true);
        initToolBar();
        initSearchView();
        initViewPager();
    }

    private void initToolBar() {
        mToolbar.setTitle("");
        ((MainActivityRx) getActivity()).setSupportActionBar(mToolbar);
        mCircleImageView.setImageResource(R.drawable.ic_avatar);
    }

    private void initSearchView() {
        //初始化SearchBar
        mSearchView.setVoiceSearch(false);
        mSearchView.setCursorDrawable(R.drawable.db_edit_cursor);
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                TotalStationSearchActivity.launch(getActivity(), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initViewPager() {
        HomePagerAdapter mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(), getApplicationContext());
        mViewPager.setOffscreenPageLimit(4); // 缓存页面数量：4+1+4  1为当前页面
        mViewPager.setAdapter(mHomeAdapter); // 设置适配器
        mSlidingTab.setupWithViewPager(mViewPager);// 关联viewPager
        mViewPager.setCurrentItem(3); // 当前显示页面
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        // 设置SearchViewItemMenu
        MenuItem item = menu.findItem(R.id.id_action_main_search);
        mSearchView.setMenuItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.id_action_main_game:
                // 游戏中心
                CommonUtil.JumpTo(GameCenterActivityRx.class);
                break;
            case R.id.id_action_main_listen:
                // 音乐中心
                CommonUtil.JumpTo(MediaPlayRx.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.home_page_navigation_layout)
    void toggleDrawer() {
        Activity activity = getActivity();
        if (activity instanceof MainActivityRx) {
            ((MainActivityRx) activity).toggleDrawer();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean isOpenSearchView() {
        return mSearchView.isSearchOpen();
    }


    public void closeSearchView() {
        mSearchView.closeSearch();
    }
}

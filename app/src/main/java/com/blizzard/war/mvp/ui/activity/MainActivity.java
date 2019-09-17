package com.blizzard.war.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.mvp.model.download.DownloadFragment;
import com.blizzard.war.mvp.model.favourite.FavouriteFragment;
import com.blizzard.war.mvp.model.group.GroupFragment;
import com.blizzard.war.mvp.model.history.HistoryFragment;
import com.blizzard.war.mvp.model.home.HomePageFragment;
import com.blizzard.war.mvp.model.setting.SettingFragment;
import com.blizzard.war.mvp.model.tracker.TrackerFragment;
import com.blizzard.war.utils.ConstantUtil;
import com.blizzard.war.utils.PreferenceUtil;
import com.blizzard.war.utils.ToastUtil;
import com.blizzard.war.mvp.ui.widget.CircleImageView;

import butterknife.BindView;

/**
 * 功能描述:
 * 首页主Activity
 *
 * @auther: ma
 * @param: MainActivity
 * @date: 2019/4/17 18:36
 */
public class MainActivity extends RxBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private Fragment[] fragments;
    private int currentTabIndex;
    private int index;
    private long exitTime;
    private HomePageFragment mHomePageFragment;
    private FavouriteFragment mFavouriteFragment;
    private GroupFragment mGroupFragment;
    private TrackerFragment mTrackerFragment;
    private DownloadFragment mDownloadFragment;
    private HistoryFragment mHistoryFragment;
    private SettingFragment mSettingFragment;
    private MenuItem menuItem;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void initViews(Bundle savedInstanceState) {
        //初始化Fragment
        initFragments();
        //初始化侧滑菜单
        initNavigationView();
    }

    /**
     * 初始化Fragments
     */
    private void initFragments() {
        mHomePageFragment = HomePageFragment.newInstance();
        mFavouriteFragment = FavouriteFragment.newInstance();
        mGroupFragment = GroupFragment.newInstance();
        mTrackerFragment = TrackerFragment.newInstance();
        mDownloadFragment = DownloadFragment.newInstance();
        mHistoryFragment = HistoryFragment.newInstance();
        mSettingFragment = SettingFragment.newInstance();


        fragments = new Fragment[]{
                mHomePageFragment,
                mFavouriteFragment,
                mGroupFragment,
                mTrackerFragment,
                mDownloadFragment,
                mHistoryFragment,
                mSettingFragment
        };
        // 添加显示第一个fragment
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mHomePageFragment)
                .show(mHomePageFragment).commit();
    }

    /**
     * 初始化NavigationView
     */
    private void initNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(this);
        menuItem = mNavigationView.getMenu().getItem(0);
        View headerView = mNavigationView.getHeaderView(0);
        CircleImageView mUserAvatarView = (CircleImageView) headerView.findViewById(R.id.user_avatar_view);
        TextView mUserName = (TextView) headerView.findViewById(R.id.user_name);
        TextView mUserSign = (TextView) headerView.findViewById(R.id.user_other_info);
        //设置头像
        mUserAvatarView.setImageResource(R.drawable.ic_avatar);
        //设置用户名 签名
        mUserName.setText(getResources().getText(R.string.vip_name));
        mUserSign.setText(getResources().getText(R.string.about_user_head_layout));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.item_home:
                // 主页
                changeFragmentIndex(item, 0);
                return true;
            case R.id.item_favourite:
                // 我的收藏
                changeFragmentIndex(item, 1);
                return true;
            case R.id.item_group:
                // 我的关注
                changeFragmentIndex(item, 2);
                return true;
            case R.id.item_tracker:
                // 我的钱包
                changeFragmentIndex(item, 3);
                return true;
            case R.id.item_download:
                // 离线缓存
                changeFragmentIndex(item, 4);
                return true;
            case R.id.item_history:
                // 历史记录
                changeFragmentIndex(item, 5);
                return true;
            case R.id.item_settings:
                // 设置中心
                changeFragmentIndex(item, 6);
                return true;
        }
        return false;
    }


    /**
     * Fragment切换
     */
    private void switchFragment() {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTabIndex]);
        if (!fragments[index].isAdded()) {
            trx.add(R.id.container, fragments[index]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
    }


    /**
     * 切换Fragment的下标
     */
    private void changeFragmentIndex(MenuItem item, int currentIndex) {
        index = currentIndex;
        switchFragment();
        item.setChecked(true);
    }


    /**
     * DrawerLayout侧滑菜单开关
     */
    public void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }


    /**
     * 监听back键处理DrawerLayout和SearchView
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLayout.getChildAt(1))) {
                mDrawerLayout.closeDrawers();
            } else {
                if (mHomePageFragment != null) {
                    if (mHomePageFragment.isOpenSearchView()) {
                        mHomePageFragment.closeSearchView();
                    } else {
                        exitApp();
                    }
                } else {
                    exitApp();
                }
            }
        }
        return true;
    }


    /**
     * 双击退出App
     */
    private void exitApp() {
        if (fragments[index] instanceof HomePageFragment) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtil.show("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                PreferenceUtil.remove(ConstantUtil.SWITCH_MODE_KEY);
                finish();
            }
        } else {
            changeFragmentIndex(menuItem, 0);
        }
    }


    /**
     * 解决App重启后导致Fragment重叠的问题
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void initToolBar() {

    }

}
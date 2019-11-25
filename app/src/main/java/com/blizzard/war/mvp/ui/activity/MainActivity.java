package com.blizzard.war.mvp.ui.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
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
import com.blizzard.war.mvp.ui.widget.CircleImageView;
import com.blizzard.war.service.AudioPlayService;
import com.blizzard.war.service.NotificationReceiver;
import com.blizzard.war.utils.ConstantUtil;
import com.blizzard.war.utils.PreferenceUtil;
import com.blizzard.war.utils.ToastUtil;

import org.json.JSONObject;

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

    public static MediaSession.Token CREATOR;
    private final String MUSIC_PREV_FILTER = "MUSIC_PREV_FILTER";
    private final String MUSIC_START_FILTER = "MUSIC_START_FILTER";
    private final String MUSIC_PAUSE_FILTER = "MUSIC_PAUSE_FILTER";
    private final String MUSIC_NEXT_FILTER = "MUSIC_NEXT_FILTER";
    private final String MUSIC_CANCEL_FILTER = "MUSIC_CANCEL_FILTER";
    private NotificationReceiver mNotificationReceiver;
    private static AudioPlayService audioPlayService;
    private static NotifiChangeListener mNotifiChangeListener;

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

        initService();
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
        CircleImageView mUserAvatarView = headerView.findViewById(R.id.user_avatar_view);
        TextView mUserName = headerView.findViewById(R.id.user_name);
        TextView mUserSign = headerView.findViewById(R.id.user_other_info);
        //设置头像
        mUserAvatarView.setImageResource(R.drawable.ic_avatar);
        //设置用户名 签名
        mUserName.setText(getResources().getText(R.string.vip_name));
        mUserSign.setText(getResources().getText(R.string.about_user_head_layout));
    }


    @Override
    public void initToolBar() {

    }

    public void initService() {
        audioPlayService = new AudioPlayService();
        audioPlayService.getAudioList();
        audioPlayService.setPlayerListener(new AudioPlayService.PlayerListener() {
            @Override
            public void isChangePlay(JSONObject s) {
                mNotifiChangeListener.isChange(s);
                setNotification();
            }

            @Override
            public void isReadChange() {
                mNotifiChangeListener.isSeedChange();
            }
        });
        setNotification();
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
                unregisterReceiver(mNotificationReceiver);
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
        super.onSaveInstanceState(outState);
    }

    public static AudioPlayService getAudioPlayService() {
        return audioPlayService;
    }

    private String getContentText() {
        return audioPlayService.getSongName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mNotificationReceiver != null) unregisterReceiver(mNotificationReceiver);
    }

    /**
     * 自定义通知栏
     */

    private void setNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String id = "audio_play";
        String name = "Bilili";
        Notification notification;
        PendingIntent pendingIntent;

        Intent resultIntent = new Intent(this, AudioPlayActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(mChannel);

            pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(MUSIC_PREV_FILTER), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action prev = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.ic_music_prev),
                    "prev",
                    pendingIntent).build();

            pendingIntent = PendingIntent.getBroadcast(this, 1, new Intent(MUSIC_START_FILTER), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action start = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.ic_music_start),
                    "start",
                    pendingIntent).build();

            pendingIntent = PendingIntent.getBroadcast(this, 2, new Intent(MUSIC_PAUSE_FILTER), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action pause = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.ic_music_pause),
                    "pause",
                    pendingIntent).build();

            pendingIntent = PendingIntent.getBroadcast(this, 3, new Intent(MUSIC_NEXT_FILTER), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action next = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.ic_music_next),
                    "next",
                    pendingIntent).build();


            pendingIntent = PendingIntent.getBroadcast(this, 4, new Intent(MUSIC_CANCEL_FILTER), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Action cancel = new Notification.Action.Builder(
                    Icon.createWithResource(this, R.drawable.ic_cancel),
                    "cancel",
                    pendingIntent).build();

            Notification.MediaStyle style = new Notification.MediaStyle();
            style.setShowActionsInCompactView(1, 2, 3);
            style.setMediaSession(CREATOR);

            int index = getContentText().indexOf("- ");
            notification = new Notification.Builder(this, id)
                    .setContentTitle(getContentText().substring(index + 2))
                    .setContentText(getContentText().substring(0, index))
                    .setContentIntent(PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setSmallIcon(R.drawable.ic_avatar)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar))
                    .addAction(prev)
                    .addAction(start)
                    .addAction(pause)
                    .addAction(next)
                    .addAction(cancel)
                    .setOngoing(true)
                    .setStyle(style)
                    .build();

            if (mNotificationReceiver == null) {
                IntentFilter intentFilter = new IntentFilter();
                mNotificationReceiver = new NotificationReceiver();
                intentFilter.addAction(MUSIC_PREV_FILTER);
                intentFilter.addAction(MUSIC_START_FILTER);
                intentFilter.addAction(MUSIC_PAUSE_FILTER);
                intentFilter.addAction(MUSIC_NEXT_FILTER);
                intentFilter.addAction(MUSIC_CANCEL_FILTER);
                registerReceiver(mNotificationReceiver, intentFilter);
                mNotificationReceiver.setNotification(i -> {
                    String s = i;
                    switch (i) {
                        case MUSIC_PREV_FILTER:
                            s = "通知栏：上一首";
                            audioPlayService.prev();
                            break;
                        case MUSIC_START_FILTER:
                            if (audioPlayService.isPause()) {
                                s = "通知栏：继续";
                                audioPlayService.goPlay();
                            } else {
                                s = "通知栏：开始";
                                audioPlayService.play();
                            }
                            break;
                        case MUSIC_PAUSE_FILTER:
                            s = "通知栏：暂停";
                            audioPlayService.pause();
                            break;
                        case MUSIC_NEXT_FILTER:
                            s = "通知栏：下一首";
                            audioPlayService.next();
                            break;
                        case MUSIC_CANCEL_FILTER:
                            s = "通知栏：关闭";
                            finish();
                            break;
                    }
                    ToastUtil.show(s);
                });
            }

        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id)
                    .setContentTitle("2 new messages")
                    .setContentText("hahaha")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX);//设置最大优先级
            notification = notificationBuilder.build();
        }
        manager.notify(1, notification);
    }

    public interface NotifiChangeListener {
        void isChange(JSONObject jsonObject);

        void isSeedChange();
    }

    public static void setNotifiChangeListener(NotifiChangeListener notifiChangeListener) {
        mNotifiChangeListener = notifiChangeListener;
    }
}
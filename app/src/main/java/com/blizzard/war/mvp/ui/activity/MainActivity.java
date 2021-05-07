package com.blizzard.war.mvp.ui.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import com.blizzard.war.mvp.model.read.ReadFragment;
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
    private ReadFragment mReadFragment;
    private MenuItem menuItem;
    private View headerView;

    public static MediaSession.Token CREATOR;
    private final String MUSIC_PREV_FILTER = "MUSIC_PREV_FILTER";
    private final String MUSIC_START_FILTER = "MUSIC_START_FILTER";
    private final String MUSIC_PAUSE_FILTER = "MUSIC_PAUSE_FILTER";
    private final String MUSIC_NEXT_FILTER = "MUSIC_NEXT_FILTER";
    private final String MUSIC_CANCEL_FILTER = "MUSIC_CANCEL_FILTER";
    private NotificationReceiver mNotificationReceiver;
    private static AudioPlayService audioPlayService;
    private static NotifyChangeListener mNotifyChangeListener;
    private Boolean isServeLive = false;
    private static final int READ_PHONE_STATE = 100;


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

        showContacts();

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
        mReadFragment = ReadFragment.newInstance();


        fragments = new Fragment[]{
                mHomePageFragment,
                mFavouriteFragment,
                mGroupFragment,
                mTrackerFragment,
                mDownloadFragment,
                mHistoryFragment,
                mSettingFragment,
                mReadFragment
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
        headerView = mNavigationView.getHeaderView(0);
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
                setNotification();
                mNotifyChangeListener.isChange(s);
            }

            @Override
            public void isReadChange() {
                mNotifyChangeListener.isSeedChange();
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
            // 阅读
            case R.id.item_read:
                changeFragmentIndex(item, 7);
                return true;
            // 视频
            case R.id.item_video:
//                CommonUtil.JumpTo(VideoPlayActivity.class);
                startRemind();
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
        } else {
            return super.onKeyDown(keyCode, event);
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
        if (isServeLive) {
            unregisterReceiver(mNotificationReceiver);
        }
        audioPlayService.stop();
        super.onDestroy();
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

            if (!isServeLive) {
                System.out.println("已注册");
                isServeLive = true;
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
                            isServeLive = false;
                            manager.cancel(1);
                            unregisterReceiver(mNotificationReceiver);
                            AudioPlayActivity._this.finish();
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

    public interface NotifyChangeListener {
        void isChange(JSONObject jsonObject);

        void isSeedChange();
    }

    public static void setNotifyChangeListener(NotifyChangeListener NotifyChangeListener) {
        mNotifyChangeListener = NotifyChangeListener;
    }


    /**
     * 开启提醒
     */
    private void startRemind() {

        Intent intent = new Intent(this, RemindActivity.class);
        startActivity(intent);

    }

    //请求权限
    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                    Manifest.permission.CHANGE_WIFI_STATE
            }, READ_PHONE_STATE);
        } else {
            //初始化音乐服务
            initService();
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case READ_PHONE_STATE:
                if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                } else {
                    initService();
                }
                break;
            default:
                break;
        }
    }
}
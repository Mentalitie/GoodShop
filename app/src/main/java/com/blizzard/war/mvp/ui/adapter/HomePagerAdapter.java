package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.blizzard.war.R;
import com.blizzard.war.mvp.model.home.Hot.HotView;
import com.blizzard.war.mvp.model.home.Live.LiveView;
import com.blizzard.war.mvp.model.home.Opera.OperaView;
import com.blizzard.war.mvp.model.home.Recommend.RecommendView;
import com.blizzard.war.mvp.model.home.ReadList.ReadListView;

/**
 * 功能描述:
 * 主界面Fragment模块Adapter
 *
 * @auther: ma
 * @param: HomePagerAdapter
 * @date: 2019/4/16 11:15
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    //Fragment集合
    private Fragment[] fragments;
    //标题
    private final String[] TITLES;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        TITLES = context.getResources().getStringArray(R.array.sections);
        fragments = new Fragment[TITLES.length];

    }


    @Override
    public Fragment getItem(int i) {
        if (fragments[i] == null) {
            switch (i) {
                case 0:
                    fragments[i] = LiveView.newInstance(); //直播
                    break;
                case 1:
                    fragments[i] = RecommendView.newInstance();//推荐
                    break;
                case 2:
                    fragments[i] = HotView.newInstance();//热门
                    break;
                case 3:
                    fragments[i] = OperaView.newInstance();//追番
                    break;
                case 4:
                    fragments[i] = ReadListView.newInstance();//分区
                    break;
                default:
                    break;
            }
        }
        return fragments[i];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }


    @Override
    public CharSequence getPageTitle(int i) {
        return TITLES[i];
    }
}
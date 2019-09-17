package com.blizzard.war.mvp.ui.widget.banner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * 功能描述:
 * Banner适配器
 *
 * @auther: ma
 * @param: BannerAdapter
 * @date: 2019/4/17 14:56
 */

public class BannerAdapter extends PagerAdapter {
    private List<ImageView> mList;
    private ViewPagerOnItemClickListener mViewPagerOnItemClickListener;

    void setmViewPagerOnItemClickListener(ViewPagerOnItemClickListener mViewPagerOnItemClickListener) {
        this.mViewPagerOnItemClickListener = mViewPagerOnItemClickListener;
    }

    BannerAdapter(List<ImageView> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回一个无限大的值，可以 无限循环
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {
        i %= mList.size();
        if (i < 0) {
            i = mList.size() + i;
        }
        ImageView v = mList.get(i);
        v.setScaleType(ImageView.ScaleType.CENTER);
        ViewParent vp = v.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(v);
        }
        v.setOnClickListener(v1 -> {
            if (mViewPagerOnItemClickListener != null) {
                mViewPagerOnItemClickListener.onItemClick();
            }
        });
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object object) {
    }

    interface ViewPagerOnItemClickListener {
        void onItemClick();
    }
}

package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.mvp.ui.widget.banner.BannerEntry;
import com.blizzard.war.mvp.ui.widget.banner.BannerView;
import com.blizzard.war.utils.CommonUtil;
import com.blizzard.war.utils.ToastUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:
 * 首页直播适配器
 *
 * @auther: ma
 * @param: LiveAdapter
 * @date: 2019/4/29 15:30
 */
public class LiveAdapter extends RecyclerView.Adapter {
    private Context context;

    //直播页Banner
    private static final int TYPE_BANNER = 0;
    //直播分类入口
    private static final int TYPE_ENTRANCE = 1;
    //直播分类Title
    private static final int TYPE_PARTITION = 2;
    //直播Item
    private static final int TYPE_LIVE_ITEM = 3;
    //导航数量
    private int entranceSize = 4;
    private List<BannerEntry> bannerEntry = new ArrayList<>();
    private List<Integer> liveSizes = new ArrayList<>();
    private String[] entranceTitles = new String[]{
            "关注主播", "直播中心",
            "搜索直播", "全部分类"
    };
    private int[] entranceIconRes = new int[]{
            R.drawable.ic_live_home_follow_anchor,
            R.drawable.ic_live_home_live_center,
            R.drawable.ic_live_home_search_room,
            R.drawable.ic_live_home_all_category
    };

    public LiveAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case TYPE_BANNER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_banner, null);
                return new LiveBannerViewHolder(view);
            case TYPE_ENTRANCE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_live_entrance, null);
                return new LiveEntranceViewHolder(view);
            case TYPE_PARTITION:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_view_partition_title, null);
                return new LivePartitionViewHolder(view);
            case TYPE_LIVE_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_card_partition, null);
                return new LiveItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i > 0) {
            i -= 1;
        }
        if (viewHolder instanceof LiveItemViewHolder) {
            LiveItemViewHolder liveItemViewHolder = (LiveItemViewHolder) viewHolder;
            Glide.with(context)
                    .load(R.drawable.ic_avatar)
                    .apply(CommonUtil.GlideInfo(context))
                    .into(liveItemViewHolder.itemCardCover);
            liveItemViewHolder.itemCardUser.setText("默认名称");
            liveItemViewHolder.itemCardTitle.setText("默认标题");
            liveItemViewHolder.itemCardCount.setText("10000");

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i % 5 % 2 == 0) {
                lp.setMargins(0, 20, 20, 20);
            } else {
                lp.setMargins(20, 20, 0, 20);
            }
            liveItemViewHolder.itemCardLayout.setLayoutParams(lp);
            liveItemViewHolder.itemCardLayout.setOnClickListener(view -> ToastUtil.show(String.valueOf(view)));
        } else if (viewHolder instanceof LivePartitionViewHolder) {
            LivePartitionViewHolder livePartitionViewHolder = (LivePartitionViewHolder) viewHolder;
            livePartitionViewHolder.itemTitle.setText("热门直播");
            livePartitionViewHolder.itemCount.setText("当前1000个直播");
        } else if (viewHolder instanceof LiveEntranceViewHolder) {
            LiveEntranceViewHolder liveEntranceViewHolder = (LiveEntranceViewHolder) viewHolder;
            liveEntranceViewHolder.title.setText(entranceTitles[i]);
            if (entranceIconRes.length > i) {
                Glide.with(context)
                        .load(entranceIconRes[i])
                        .apply(CommonUtil.GlideInfo(context))
                        .into(liveEntranceViewHolder.image);
            }
        } else if (viewHolder instanceof LiveBannerViewHolder) {
            LiveBannerViewHolder liveBannerViewHolder = (LiveBannerViewHolder) viewHolder;
            bannerEntry.clear();
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            liveBannerViewHolder.banner
                    .delayTime(5)
                    .build(bannerEntry, context);
        }
    }

    public int getItemCount() {
        return 1 + entranceIconRes.length
                + 20 * 5;
    }

    public int getSpanSize(int pos) {
        int viewType = getItemViewType(pos);
        switch (viewType) {
            case TYPE_BANNER:
                return 12;
            case TYPE_ENTRANCE:
                return 3;
            case TYPE_PARTITION:
                return 12;
            case TYPE_LIVE_ITEM:
                return 6;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int i) {
        if (i == 0) {
            return TYPE_BANNER;
        }
        i -= 1;
        if (i < entranceSize) {
            return TYPE_ENTRANCE;
        } else if (isPartitionTitle(i)) {
            return TYPE_PARTITION;
        } else {
            return TYPE_LIVE_ITEM;
        }
    }

    /**
     * 获取当前Item在第几组中
     */
    private int getItemPosition(int pos) {
        pos -= entranceSize;
        return pos / 5;
    }


    private boolean isPartitionTitle(int pos) {
        pos -= entranceSize;
        return (pos % 5 == 0);
    }

    /**
     * 初始化数据
     */
    public void setLiveInfo() {
        liveSizes.clear();
        bannerEntry.clear();
        entranceSize = 4;
    }

    /**
     * 直播界面Banner ViewHolder
     */
    static class LiveBannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_banner)
        public BannerView banner;

        LiveBannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 直播界面Item分类 ViewHolder
     */
    static class LiveEntranceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.live_entrance_title)
        public TextView title;
        @BindView(R.id.live_entrance_image)
        public ImageView image;

        LiveEntranceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 直播界面Grid Item ViewHolder
     */
    static class LiveItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_card_cover)
        ImageView itemCardCover;
        @BindView(R.id.item_card_user)
        TextView itemCardUser;
        @BindView(R.id.item_card_title)
        TextView itemCardTitle;
        @BindView(R.id.item_card_count)
        TextView itemCardCount;
        @BindView(R.id.item_card_layout)
        CardView itemCardLayout;

        LiveItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 直播界面分区类型 ViewHolder
     */
    static class LivePartitionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view_partition_title)
        TextView itemTitle;
        @BindView(R.id.item_view_partition_count)
        TextView itemCount;

        LivePartitionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

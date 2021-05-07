package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
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
import com.blizzard.war.utils.DisplayUtil;
import com.blizzard.war.utils.ToastUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:
 * 首页追番适配器
 *
 * @auther: ma
 * @param: OperaAdapter
 * @date: 2019/4/29 15:30
 */

public class OperaAdapter extends RecyclerView.Adapter {
    private Context context;

    // banner
    private static final int TYPE_BANNER = 0;

    // 图标导航
    private static final int TYPE_NAV_ITEM = 1;

    // item 头部
    private static final int TYPE_PART_TITLE = 2;

    // gird item
    private static final int TYPE_PART_ITEM = 3;

    // banner 模型
    private List<BannerEntry> bannerEntry = new ArrayList<>();

    public OperaAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case TYPE_BANNER:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_banner, null);
                return new OperaBannerViewHolder(view);
            case TYPE_NAV_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_opera_gird_nav, null);
                return new OperaNavViewHolder(view);
            case TYPE_PART_TITLE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_view_partition_title, null);
                return new OperaHeadViewHolder(view);
            case TYPE_PART_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_paper_partition, null);
                return new OperaItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof OperaBannerViewHolder) {
            OperaBannerViewHolder operaBannerViewHolder = (OperaBannerViewHolder) viewHolder;
            bannerEntry.clear();
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            bannerEntry.add(new BannerEntry("图片1", "https://www.baidu.com", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F57d22dede8e68.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1622376756&t=3378131e48184d1eaa2f1f4405174655"));
            operaBannerViewHolder.banner
                    .delayTime(5)
                    .build(bannerEntry, context);
        } else if (viewHolder instanceof OperaNavViewHolder) {
            OperaNavViewHolder operaNavViewHolder = (OperaNavViewHolder) viewHolder;
            // 新增前清空数据
            operaNavViewHolder.itemOperaNav.removeAllViews();
            operaNavViewHolder.itemScrollView.removeAllViews();
            operaNavViewHolder.itemTitle.setText("我的追番");
            operaNavViewHolder.itemCount.setText("查看全部");
            for (int j = 0; j < 5; j++) {
                LayoutInflater inflater = LayoutInflater.from(context);
                // 获取需要添加的布局
                LinearLayout layout = (LinearLayout) inflater.inflate(
                        R.layout.item_opera_gird_item, null).findViewById(R.id.item_opera_gird);

                layout.setOnClickListener(view -> onClick(view.getId()));
                TextView navText = (TextView) layout.findViewById(R.id.item_opera_gird_text);
                switch (j) {
                    case 0:
                        navText.setText("番剧");
                        layout.setId(R.id.operaNav_1);
                        break;
                    case 1:
                        navText.setText("国创");
                        layout.setId(R.id.operaNav_2);
                        break;
                    case 2:
                        navText.setText("时间表");
                        layout.setId(R.id.operaNav_3);
                        break;
                    case 3:
                        navText.setText("索引");
                        layout.setId(R.id.operaNav_4);
                        break;
                    case 4:
                        navText.setText("点评");
                        layout.setId(R.id.operaNav_5);
                        break;
                }

                //此处我需要均分宽度就在width处设0,1.0f即设置权重是1
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                // 将布局加入到当前布局中
                operaNavViewHolder.itemOperaNav.addView(layout, layoutParams);

                layoutParams = new LinearLayout.LayoutParams(DisplayUtil.dp2px(context,120), LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, DisplayUtil.dp2px(context,10), 0);
                layout = (LinearLayout) inflater.inflate(
                        R.layout.item_opera_scroll_view, null);

                operaNavViewHolder.itemScrollView.addView(layout, layoutParams);
            }
        } else if (viewHolder instanceof OperaHeadViewHolder) {
            OperaHeadViewHolder operaHeadViewHolder = (OperaHeadViewHolder) viewHolder;
            operaHeadViewHolder.itemTitle.setText("番剧推荐");
            operaHeadViewHolder.itemCount.setText("查看更多");
        } else if (viewHolder instanceof OperaItemViewHolder) {
            OperaItemViewHolder operaItemViewHolder = (OperaItemViewHolder) viewHolder;
            Glide.with(context)
                    .load(R.drawable.ic_avatar)
                    .apply(CommonUtil.GlideInfo(context))
                    .into(operaItemViewHolder.itemPaperCover);
            operaItemViewHolder.itemPaperTitle.setText("默认标题");
            operaItemViewHolder.itemPaperUser.setText("默认名称");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i % 5 == 3 || i % 5 == 0) {
                lp.setMargins(0, 0, 15, 20);
            } else {
                lp.setMargins(15, 0, 0, 20);
            }
            operaItemViewHolder.itemPaperLayout.setLayoutParams(lp);

        }
    }

    private void onClick(int id) {
        switch (id) {
            case R.id.operaNav_1:
                ToastUtil.show("番剧");
                break;
            case R.id.operaNav_2:
                ToastUtil.show("国创");
                break;
            case R.id.operaNav_3:
                ToastUtil.show("时间表");
                break;
            case R.id.operaNav_4:
                ToastUtil.show("索引");
                break;
            case R.id.operaNav_5:
                ToastUtil.show("点评");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2 + 20 * 5;
    }

    public int getSpanSize(int pos) {
        int viewType = getItemViewType(pos);
        switch (viewType) {
            case TYPE_BANNER:
                return 2;
            case TYPE_NAV_ITEM:
                return 2;
            case TYPE_PART_TITLE:
                return 2;
            case TYPE_PART_ITEM:
                return 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int i) {
        if (i == 0) {
            return TYPE_BANNER;
        }
        i -= 1;
        if (i < 1) {
            return TYPE_NAV_ITEM;
        } else if (isPartitionTitle(i)) {
            return TYPE_PART_TITLE;
        } else {
            return TYPE_PART_ITEM;
        }
    }

    private boolean isPartitionTitle(int pos) {
        pos -= 1;
        return (pos % 5 == 0);
    }

    /**
     * 追番界面 Grid banner ViewHolder
     */
    static class OperaBannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_banner)
        public BannerView banner;


        OperaBannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 追番界面 Grid nav ViewHolder
     */
    static class OperaNavViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_opera_nav)
        LinearLayout itemOperaNav;
        @BindView(R.id.item_view_partition_title)
        TextView itemTitle;
        @BindView(R.id.item_view_partition_count)
        TextView itemCount;
        @BindView(R.id.item_opera_scroll_view)
        LinearLayout itemScrollView;

        OperaNavViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 追番界面 Grid Item ViewHolder
     */
    static class OperaItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_paper_cover)
        ImageView itemPaperCover;
        @BindView(R.id.item_paper_user)
        TextView itemPaperUser;
        @BindView(R.id.item_paper_title)
        TextView itemPaperTitle;
        @BindView(R.id.item_paper_layout)
        LinearLayout itemPaperLayout;

        OperaItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 追番界面 Item Head ViewHolder
     */
    static class OperaHeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view_partition_title)
        TextView itemTitle;
        @BindView(R.id.item_view_partition_count)
        TextView itemCount;

        OperaHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

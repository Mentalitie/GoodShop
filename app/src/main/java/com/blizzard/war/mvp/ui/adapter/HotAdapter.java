package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blizzard.war.R;
import com.blizzard.war.utils.CommonUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blizzard.war.utils.CommonUtil.GetColor;
import static com.blizzard.war.utils.CommonUtil.GetDrawable;

/**
 * 功能描述:
 * 首页热门适配器
 *
 * @auther: ma
 * @param: HotAdapter
 * @date: 2019/4/29 15:30
 */

public class HotAdapter extends RecyclerView.Adapter {
    private Context context;

    private static final int TYPE_PART_TITLE = 0;

    private static final int TYPE_PART_ITEM = 1;


    public HotAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case TYPE_PART_TITLE:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_view_partition_title, null);
                return new HotHeadViewHolder(view);
            case TYPE_PART_ITEM:
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_view_partition, null);
                return new HotItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HotHeadViewHolder) {
            HotHeadViewHolder hotHeadViewHolder = (HotHeadViewHolder) viewHolder;
            hotHeadViewHolder.itemTitle.setText("当前热门");
            hotHeadViewHolder.itemCount.setText("排行榜");
            hotHeadViewHolder.itemCount.setTextColor(GetColor(R.color.text_default));
            hotHeadViewHolder.itemRank.setImageDrawable(GetDrawable(R.drawable.ic_tm_rank));
            hotHeadViewHolder.itemArrow.setVisibility(View.GONE);
            hotHeadViewHolder.itemArrowLight.setVisibility(View.VISIBLE);
        } else {
            HotItemViewHolder hotItemViewHolder = (HotItemViewHolder) viewHolder;
            Glide.with(context)
                    .load(R.drawable.ic_avatar)
                    .apply(CommonUtil.GlideInfo(context))
                    .into(hotItemViewHolder.itemViewImageCover);
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public int getSpanSize(int pos) {
        return 1;
    }

    @Override
    public int getItemViewType(int i) {
        if (i == 0) {
            return TYPE_PART_TITLE;
        } else {
            return TYPE_PART_ITEM;
        }
    }

    /**
     * 热门界面Grid Item ViewHolder
     */
    static class HotItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view_image_cover)
        ImageView itemViewImageCover;

        HotItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 热门界面 Item Head ViewHolder
     */
    static class HotHeadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_view_partition_title)
        TextView itemTitle;
        @BindView(R.id.item_view_partition_count)
        TextView itemCount;
        @BindView(R.id.item_view_rank)
        ImageView itemRank;
        @BindView(R.id.item_view_arrow)
        ImageView itemArrow;
        @BindView(R.id.item_view_arrow_light)
        ImageView itemArrowLight;

        HotHeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

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
import com.blizzard.war.utils.CommonUtil;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:
 * 首页推荐适配器
 *
 * @auther: ma
 * @param: RecommendAdapter
 * @date: 2019/4/29 15:30
 */
public class RecommendAdapter extends RecyclerView.Adapter {
    private Context context;

    public RecommendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_card_partition, null);
        return new RecommendAdapter.GridItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        GridItemViewHolder gridItemViewHolder = (GridItemViewHolder) viewHolder;
        Glide.with(context)
                .load(R.drawable.ic_avatar)
                .apply(CommonUtil.GlideInfo(context))
                .into(gridItemViewHolder.itemCardCover);
        gridItemViewHolder.itemCardUser.setText("默认名称");
        gridItemViewHolder.itemCardTitle.setText("默认标题");
        gridItemViewHolder.itemCardCount.setText("10000");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (i % 2 == 0) {
            lp.setMargins(0, 20, 20, 20);
        } else {
            lp.setMargins(20, 20, 0, 20);
        }
        gridItemViewHolder.itemCardLayout.setLayoutParams(lp);
    }

    @Override
    public int getItemCount() {
        return 20 * 5;
    }

    public int getSpanSize(int pos) {
        return 1;
    }

    /**
     * 推荐界面Grid Item ViewHolder
     */
    static class GridItemViewHolder extends RecyclerView.ViewHolder {
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

        GridItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

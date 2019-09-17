package com.blizzard.war.mvp.ui.adapter.pager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blizzard.war.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:
 * 首页分区适配器
 *
 * @auther: ma
 * @param: RegionAdapter
 * @date: 2019/4/29 15:30
 */

public class RegionAdapter extends RecyclerView.Adapter {
    private Context context;

    public RegionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_region_gird_item, null);
        return new RegionAdapter.GridItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        GridItemViewHolder gridItemViewHolder = (GridItemViewHolder) viewHolder;
        gridItemViewHolder.itemGirdText.setText("标题");
    }

    @Override
    public int getItemCount() {
        return 8 * 4;
    }

    public int getSpanSize(int pos) {
        return 1;
    }

    /**
     * 推荐界面Grid Item ViewHolder
     */
    static class GridItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_region_gird_image)
        ImageView itemGirdImage;
        @BindView(R.id.item_region_gird_text)
        TextView itemGirdText;

        GridItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

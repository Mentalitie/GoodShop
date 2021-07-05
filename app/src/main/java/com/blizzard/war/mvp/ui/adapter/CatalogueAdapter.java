package com.blizzard.war.mvp.ui.adapter;

import android.annotation.SuppressLint;
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
import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.utils.CommonUtil;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blizzard.war.utils.CommonUtil.GetDrawable;

/**
 * 功能描述:
 *
 * @auther: ma
 * @param: CatalogueAdapter
 * @date: 2019/4/29 15:30
 */

public class CatalogueAdapter extends RecyclerView.Adapter {
    private Context context;
    private SelectItem mSelectItem;
    private int mPosition;

    private List<ReadEntry> txtList;

    public CatalogueAdapter(Context context, List<ReadEntry> list) {
        this.context = context;
        this.txtList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_read_text_gird, null);
        return new CatalogueAdapter.CatalogueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CatalogueViewHolder) {
            CatalogueViewHolder catalogueViewHolder = (CatalogueViewHolder) viewHolder;
            catalogueViewHolder.mTextView.setText(txtList.get(i).getTitle());
            catalogueViewHolder.llReadGird.setBackground(CommonUtil.GetDrawable(R.drawable.db_line_bottom));
            catalogueViewHolder.llReadGird.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectItem.select(v, i);
                }
            });
        }
    }


    public void setViewBackColor() {

    }

    @Override
    public int getItemCount() {
        return txtList.size();
    }

    public int getSpanSize(int pos) {
        return 1;
    }


    public interface SelectItem {
        void select(View view, int i);
    }

    public void setSelectItem(SelectItem selectItem) {
        mSelectItem = selectItem;
    }

    /**
     * Grid Item ViewHolder
     */
    static class CatalogueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_read_text)
        TextView mTextView;
        @BindView(R.id.ll_read_gird)
        LinearLayout llReadGird;

        CatalogueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

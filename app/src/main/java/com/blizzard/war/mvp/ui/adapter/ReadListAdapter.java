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
import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.utils.CommonUtil;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blizzard.war.utils.CommonUtil.GetDrawable;

/**
 * 功能描述:
 * 首页阅读适配器
 *
 * @auther: ma
 * @param: ReadListAdapter
 * @date: 2019/4/29 15:30
 */

public class ReadListAdapter extends RecyclerView.Adapter {
    private Context context;
    private SelectItem mSelectItem;
    private int mPosition;

    private List<ReadEntry> txtList;

    public ReadListAdapter(Context context, List<ReadEntry> list) {
        this.context = context;
        this.txtList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_read_list, null);
        return new ReadListAdapter.GridItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        GridItemViewHolder gridItemViewHolder = (GridItemViewHolder) viewHolder;
        Glide.with(context)
                .load(R.drawable.ic_avatar)
                .apply(CommonUtil.GlideInfo(context))
                .into(gridItemViewHolder.ivBack);
        gridItemViewHolder.tvRead.setText(txtList.get(i).getTitle());
//        setSelect(gridItemViewHolder, txtList.get(i).getSelect());
        setComplete(gridItemViewHolder, txtList.get(i).getComplete());
        gridItemViewHolder.cvCardBox.setOnClickListener(v -> {
            mPosition = i;
            mSelectItem.select(v, i);
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (i % 2 == 0) {
            lp.setMargins(0, 20, 20, 20);
        } else {
            lp.setMargins(20, 20, 0, 20);
        }
        gridItemViewHolder.cvCardBox.setLayoutParams(lp);


    }

    private void setSelect(GridItemViewHolder gridItemViewHolder, Boolean bo) {
        if (bo) {
            gridItemViewHolder.cvCardBox.setBackground(GetDrawable(R.color.window_tool_bar_icon));
        } else {
            gridItemViewHolder.cvCardBox.setBackground(GetDrawable(R.color.window_background));
        }
    }

    private void setComplete(GridItemViewHolder gridItemViewHolder, Boolean bo) {
        if (bo) {
            gridItemViewHolder.tvReadComplete.setVisibility(View.VISIBLE);
        } else {
            gridItemViewHolder.tvReadComplete.setVisibility(View.GONE);
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
     * 推荐界面Grid Item ViewHolder
     */
    static class GridItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_card_box)
        CardView cvCardBox;
        @BindView(R.id.iv_back)
        ImageView ivBack;
        @BindView(R.id.tv_read)
        TextView tvRead;
        @BindView(R.id.tv_read_complete)
        TextView tvReadComplete;

        GridItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

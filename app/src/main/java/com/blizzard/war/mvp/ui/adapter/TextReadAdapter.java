package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blizzard.war.R;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:
 * 阅读适配器
 *
 * @auther: ma
 * @param: TextReadAdapter
 * @date: 2021-04-20 17:02
 */

public class TextReadAdapter extends RecyclerView.Adapter {
    private Context context;
    private SelectItem mSelectItem;
    private int mPosition;


    private  String[] txtList;


    public TextReadAdapter(Context context,  String[] list) {
        this.context = context;
        this.txtList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_read_text_gird, null);
        return new TxtReadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TxtReadViewHolder) {
            TxtReadViewHolder txtReadViewHolder = (TxtReadViewHolder) viewHolder;
            if (txtList[i].length() > 3) {
//                txtReadViewHolder.tvReadText.setText(txtList[i]);
                txtReadViewHolder.tvReadText.setText(txtList[i].replace("”", "").replace("“", "") + "。");
            } else {
                txtReadViewHolder.tvReadText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return txtList.length;
    }

    public int getSpanSize(int pos) {
        return 1;
    }

    public void setViewBackColor() {

    }

    public interface SelectItem {
        void select(View view, int i);
    }

    public void setSelectItem(SelectItem selectItem) {
        mSelectItem = selectItem;
    }


    /**
     * 阅读 Grid banner ViewHolder
     */
    static class TxtReadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_read_text)
        TextView tvReadText;

        TxtReadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

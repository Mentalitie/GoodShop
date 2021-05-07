package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blizzard.war.R;


import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blizzard.war.utils.CommonUtil.GetDrawable;

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

    // banner 模型
    private List<JSONObject> txtList;
    private Boolean listBo;

    public TextReadAdapter(Context context, List<JSONObject> list, Boolean bo) {
        this.context = context;
        this.txtList = list;
        this.listBo = bo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_read_gird, null);
        return new TxtReadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof TxtReadViewHolder) {
            TxtReadViewHolder txtReadViewHolder = (TxtReadViewHolder) viewHolder;
            txtReadViewHolder.tvRead.setText(txtList.get(i).getString("title"));
            setSelect(txtReadViewHolder,txtList.get(i).getBoolean("isSelect"));
            txtReadViewHolder.tvRead.setOnClickListener(v -> {
                mPosition = i;
                mSelectItem.select(v, i, listBo);
            });
        }
    }

    private void setSelect(TxtReadViewHolder txtReadViewHolder,Boolean bo){
        if (bo) {
            txtReadViewHolder.tvRead.setBackground(GetDrawable(R.color.window_tool_bar_icon));
        } else {
            txtReadViewHolder.tvRead.setBackground(GetDrawable(R.color.window_background));
        }
    }


    @Override
    public int getItemCount() {
        return txtList.size();
    }

    public int getSpanSize(int pos) {
        return 1;
    }

    public void setViewBackColor() {

    }

    public interface SelectItem {
        void select(View view, int i, Boolean bo);
    }

    public void setSelectItem(SelectItem selectItem) {
        mSelectItem = selectItem;
    }


    /**
     * 阅读 Grid banner ViewHolder
     */
    static class TxtReadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_read)
        TextView tvRead;

        TxtReadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

package com.blizzard.war.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blizzard.war.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能描述:
 * 音乐中心适配器
 *
 * @auther: ma
 * @param: OperaAdapter
 * @date: 2019/4/29 15:30
 */

public class AudioPlayAdapter extends RecyclerView.Adapter {
    private Context context;
    private SelectItem mSelectItem;
    private int mPosition;

    // banner 模型
    private List<JSONObject> musicList;

    public AudioPlayAdapter(Context context, List<JSONObject> list) {
        this.context = context;
        this.musicList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_music_gird, null);
        return new MusicListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof MusicListViewHolder) {
            MusicListViewHolder musicListViewHolder = (MusicListViewHolder) viewHolder;
            try {
                musicListViewHolder.itemText.setText(musicList.get(i).getString("song_name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            musicListViewHolder.itemText.setOnClickListener(v -> {
                mPosition = i;
                mSelectItem.select(v, i);
            });
        }
    }


    @Override
    public int getItemCount() {
        return musicList.size();
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
     * 音乐中心界面 Grid banner ViewHolder
     */
    static class MusicListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_music_text)
        TextView itemText;

        MusicListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

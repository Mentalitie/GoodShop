package com.blizzard.war.mvp.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aliyun.tts.AliTtsOnLine;
import com.blizzard.war.R;
import com.blizzard.war.app.BiliApplication;
import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.mvp.ui.adapter.TextReadAdapter;
import com.blizzard.war.utils.FileUtil;
import com.blizzard.war.utils.GsonUtils;
import com.blizzard.war.utils.SystemBarHelper;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;

import static com.blizzard.war.utils.CommonUtil.GetColor;

/**
 * 功能描述:
 * 阅读模块主界面
 *
 * @auther: ma
 * @param: ReadActivity
 * @date: 2021-04-19 12:25
 */
public class ReadActivity extends RxBaseActivity {

    //    @BindView(R.id.layout_menu_icon)
//    ImageView mMenuICon;
//    @BindView(R.id.layout_back_icon)
//    ImageView mBackICon;
//    @BindView(R.id.layout_tool_title)
//    TextView mToolTitle;
    @BindView(R.id.rv_showText)
    RecyclerView mRvShowText;

    private TextToSpeech mTextToSpeech;
    private TextReadAdapter mainAdapter;
    private ReadEntry readEntry;
    private MediaPlayer player;
    final String RE = "([第].{1,5}[章])(.+)";//    line.trim().matches(RE)
    private List<ReadEntry> list;
    private String[] readList;
    private AliTtsOnLine aliTtsOnLine;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
//        SystemBarHelper.tintStatusBar(this, GetColor(R.color.read_text_back), 0);
        SystemBarHelper.setStatusBarDarkMode(this, true);
        bottomSheetDialog.show();
        Intent intent = getIntent();
        List<ReadEntry> li = BiliApplication.getReadList();
        readEntry = li.get(intent.getIntExtra("index", 0));
        list = GsonUtils.fromJson(readEntry.getChild(), new TypeToken<List<ReadEntry>>() {
        }.getType());
        try {
            readList = FileUtil.readFileAsString(list.get(0).getPath()).replace("<br>", "").split("。");
            aliTtsOnLine = new AliTtsOnLine(this);
            playTts();

//            readList = FileUtil.readFileAsLine(list.get(0).getPath());
            initList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initToolBar() {
//        mBackICon.setVisibility(View.VISIBLE);
//        mMenuICon.setVisibility(View.GONE);
//        mBackICon.setImageDrawable(CommonUtil.GetDrawable(R.drawable.ic_cancel));
//        mToolTitle.setText(readEntry.getTitle());
//        mBackICon.setOnClickListener(v -> finish());
    }

    private void initList() {
        mainAdapter = new TextReadAdapter(this, readList);
        mRvShowText.setAdapter(mainAdapter);
        mainAdapter.setSelectItem((view, i) -> {

//            changeReadList(li.get(i).getChild());
        });
        GridLayoutManager layout = new GridLayoutManager(this, 1);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return mainAdapter.getSpanSize(i);
            }
        });
        mRvShowText.setLayoutManager(layout);
    }

    private void playTts() {
        final int[] index = {0};

        aliTtsOnLine.play(readList[index[0]]);
        aliTtsOnLine.setListen(new AliTtsOnLine.PlayOver() {
            @Override
            public void over() {
                index[0]++;
                aliTtsOnLine.play(readList[index[0]]);
            }
        });

    }

    private void BottomSheetInit() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        View view = View.inflate(this, R.layout.dialog_bottom_sheet, null);
        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomDialog);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(peekHeight - peekHeight / 3);
    }

    @Override
    protected void onDestroy() {
        if (aliTtsOnLine != null) aliTtsOnLine.stop();
        super.onDestroy();
    }
}

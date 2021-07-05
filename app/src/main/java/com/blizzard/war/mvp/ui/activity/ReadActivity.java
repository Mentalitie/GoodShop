package com.blizzard.war.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.aliyun.tts.AudioPlayer;
import com.aliyun.tts.AudioPlayerCallback;
import com.blizzard.war.R;
import com.blizzard.war.app.BiliApplication;
import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.mvp.contract.RxBaseActivity;
import com.blizzard.war.mvp.ui.adapter.CatalogueAdapter;
import com.blizzard.war.mvp.ui.adapter.TextReadAdapter;
import com.blizzard.war.service.ReadDownLoadIntentService;
import com.blizzard.war.utils.BaiduTTS;
import com.blizzard.war.utils.FileUtil;
import com.blizzard.war.utils.GsonUtils;
import com.blizzard.war.utils.SystemBarHelper;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述:
 * 阅读模块主界面
 *
 * @auther: ma
 * @param: ReadActivity
 * @date: 2021-04-19 12:25
 */
public class ReadActivity extends RxBaseActivity {

    @BindView(R.id.rv_showText)
    RecyclerView mRvShowText;
    @BindView(R.id.bt_catalogue_show)
    Button mBtnBook;

    private TextReadAdapter mainAdapter;
    private ReadEntry readEntry;
    final String RE = "([第].{1,5}[章])(.+)";//    line.trim().matches(RE)
    private List<ReadEntry> list;
    private List<JSONObject> readList = new ArrayList<>();
    //    private AliTtsOnLine aliTtsOnLine;
    private BottomSheetDialog bottomSheetDialog;
    private int position = 0;
    private int[] textPosition = {0};
    private Boolean isShowBtn = false;
    private Boolean isScroll = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        SystemBarHelper.setTransparent(this);
        Intent intent = getIntent();
        List li = BiliApplication.getReadList();
        readEntry = (ReadEntry) li.get(intent.getIntExtra("index", 0));
        list = GsonUtils.fromJson(readEntry.getChild(), new TypeToken<List<ReadEntry>>() {
        }.getType());
        BottomSheetInit();

        initStringList(position);
//        aliTtsOnLine = new AliTtsOnLine(this);
//        playTts();
        initList();

//        Intent intent1 = new Intent(this, ReadDownLoadIntentService.class);
//        intent1.putExtra("txtPath", readEntry.getChild());
//        startService(intent1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BaiduTTS baiduTTS = new BaiduTTS();
                final int[] num = {0};
                baiduTTS.process(readList.get(num[0]).getString("text"), Environment.getExternalStorageDirectory().getPath() + File.separator + "/123.mp3");
                baiduTTS.setBaiduTTSListener(new BaiduTTS.BaiduTTSListener() {
                    @Override
                    public void isComplete() {
                        num[0]++;
                        baiduTTS.process(readList.get(num[0]).getString("text"), Environment.getExternalStorageDirectory().getPath() + File.separator + "/123.mp3");
                    }

                    @Override
                    public void isShutDown() {

                    }
                });
            }
        });
        thread.start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRvShowText.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    isScroll = true;
                }
            });
        }
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    position++;
                    textPosition = new int[]{0};
                    mRvShowText.scrollToPosition(0);
                    releaseText();
                    break;
            }
            return false;
        }
    });

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
            isShowBtn = !isShowBtn;
            if (isShowBtn) {
                mBtnBook.setVisibility(View.VISIBLE);
            } else {
                isScroll = false;
                mBtnBook.setVisibility(View.GONE);
            }
            textPosition[0] = i;
//            aliTtsOnLine.stop();
            playTts();
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
        JSONObject jsonObject = (JSONObject) readList.get(textPosition[0]);
//        aliTtsOnLine.play(jsonObject.getString("text"));
//        aliTtsOnLine.setListen(new AliTtsOnLine.PlayOver() {
//            @Override
//            public void over() {
//                if (textPosition[0] >= (readList.size() - 1)) {
//                    Message message = new Message();
//                    message.what = 1;
//                    handler.sendMessage(message);
//                } else {
//                    textPosition[0]++;
//                    JSONObject jsonObject = readList.get(textPosition[0]);
//                    aliTtsOnLine.play(jsonObject.getString("text"));
//                    if (!isScroll) {
//                        mRvShowText.smoothScrollToPosition(textPosition[0]);
//                    }
//                }
//            }
//        });
    }

    private void initStringList(int i) {
        try {
            readList.clear();
            String[] s = FileUtil.readFileAsString(list.get(i).getPath()).replace("<br>", "").split("。");
            List<JSONObject> rs = new ArrayList<>();
            for (int j = 0; j < s.length; j++) {
                if (!s[j].contains("网址") && !s[j].contains("首发域名") && !s[j].contains("打赏") && !s[j].contains("月票") && !s[j].contains("会员") && !s[j].contains("收藏")) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("text", s[j]);
                    rs.add(jsonObject);
                }
            }
            readList.addAll(rs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseText() {
        initStringList(position);
        mainAdapter.notifyDataSetChanged();
//        aliTtsOnLine.stop();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                playTts();
            }
        });
        thread.start();
    }

    private void BottomSheetInit() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        View view = View.inflate(this, R.layout.dialog_bottom_sheet, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_catalogue);
        CatalogueAdapter adapter = new CatalogueAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setSelectItem((v, i) -> {
            bottomSheetDialog.hide();
            position = i;
            releaseText();
        });
        GridLayoutManager layout = new GridLayoutManager(this, 1);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return adapter.getSpanSize(i);
            }
        });
        recyclerView.setLayoutManager(layout);

        bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomDialog);
        bottomSheetDialog.setContentView(view);
        BottomSheetBehavior<View> mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
        mDialogBehavior.setPeekHeight(peekHeight - peekHeight / 3);
    }

    @OnClick(R.id.bt_catalogue_show)
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_catalogue_show:
                bottomSheetDialog.show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
//        if (aliTtsOnLine != null) aliTtsOnLine.releaseNui();
        super.onDestroy();
    }
}

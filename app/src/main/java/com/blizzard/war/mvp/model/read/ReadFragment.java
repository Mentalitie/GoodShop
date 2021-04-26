package com.blizzard.war.mvp.model.read;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blizzard.war.R;
import com.blizzard.war.mvp.contract.RxLazyFragment;
import com.blizzard.war.mvp.ui.activity.MainActivity;
import com.blizzard.war.mvp.ui.adapter.TextReadAdapter;
import com.blizzard.war.utils.DoubleClickUtil;
import com.blizzard.war.utils.GsonUtils;
import com.blizzard.war.utils.LogUtil;
import com.blizzard.war.utils.ReadTextUtil;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述:
 * 阅读模块主界面
 *
 * @auther: ma
 * @param: ReadFragment
 * @date: 2021-04-19 12:25
 */
public class ReadFragment extends RxLazyFragment implements TextToSpeech.OnInitListener {
    @BindView(R.id.layout_menu_icon)
    ImageView mMenuIcon;
    @BindView(R.id.layout_tool_title)
    TextView mToolTitle;
    @BindView(R.id.layout_toolbar)
    Toolbar mToolBar;
    @BindView(R.id.rv_showText)
    RecyclerView mRvShowText;
    @BindView(R.id.rv_showChild)
    RecyclerView mRvShowChild;
    @BindView(R.id.sb_speed)
    SeekBar mSpeed;
    @BindView(R.id.sb_voice)
    SeekBar mVoice;

    private Message message = new Message();
    private List<JSONObject> list = new ArrayList<>();
    private List<JSONObject> readList;
    private TextToSpeech mTextToSpeech;
    private TextReadAdapter textReadAdapter;
    private Handler handler;
    private int pageIndex = 0;
    private Boolean isSelect = false;

    public static ReadFragment newInstance() {
        return new ReadFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_read;
    }

    @Override
    public void finishCreateView(Bundle state) {
        mToolTitle.setText("阅读");
        mMenuIcon.setOnClickListener(v -> {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).toggleDrawer();
            }
        });

        mToolBar.setOnClickListener(new DoubleClickUtil() {
            @Override
            public void onDoubleClick(View v) {
                mRvShowChild.smoothScrollToPosition(0);
            }
        });
        initSpeech();
    }

    @SuppressLint("HandlerLeak")
    protected void initSpeech() {
        GetList();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        System.out.println("线程结束");
                        initList(list, false);
                        break;
                    case 0:
                        System.out.println("失败");
                        break;
                }
            }
        };
        mTextToSpeech = new TextToSpeech(getContext(), this);
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mTextToSpeech.setPitch(2f);
        // 设置语速
        mTextToSpeech.setSpeechRate(0.9f);
        mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                final Activity a = (Activity) getContext();
                a.runOnUiThread(new Runnable() {
                    public void run() {
                        if (!isSelect) {
                            pageIndex++;
                            speech(readList.get(pageIndex).getString("path"));
                        }
                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    private void initList(List<JSONObject> li, Boolean bo) {
        textReadAdapter = new TextReadAdapter(getContext(), li);
        if (!bo) {
            mRvShowText.setAdapter(textReadAdapter);
        } else {
            mRvShowChild.setAdapter(textReadAdapter);
        }
        textReadAdapter.setSelectItem((view, i) -> {
            if (!bo) {
                List<JSONObject> list1 = GsonUtils.fromJson(li.get(i).getString("child"), new TypeToken<List<JSONObject>>() {
                }.getType());
                initList(list1, true);
            } else {
                readList = li;
                isSelect = true;
                speech(readList.get(i).getString("path"));
                pageIndex = i;
            }

        });
        GridLayoutManager layout = new GridLayoutManager(getContext(), 1);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return textReadAdapter.getSpanSize(i);
            }
        });
        if (!bo) {
            mRvShowText.setLayoutManager(layout);
        } else {
            mRvShowChild.setLayoutManager(layout);
        }
    }

    private void GetList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                /**要实现的方法*/
                ContentResolver contentResolver = getContext().getContentResolver();
                Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), null, "mime_type=\"text/plain\"", null, MediaStore.Files.FileColumns.TITLE);
                String filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Story/";
                File[] file = new File(filePath).listFiles();

                JSONArray listArray = new JSONArray();
                for (int i = 0; i < file.length; i++) {
                    JSONObject listObject = new JSONObject();
                    listObject.put("title", file[i].getName());
                    listObject.put("path", filePath + file[i].getName());

                    while (cursor.moveToNext()) {
                        Map<String, Object> mMap = new HashMap<String, Object>();
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        if (path.contains(filePath)) {
                            mMap.put("title", title);
                            mMap.put("path", path);
//                            String regEx = "[^0-9]";
//                            Pattern p = Pattern.compile(regEx);
//                            Matcher m = p.matcher(title);
//                            m.replaceAll("").trim()
                            Pattern ps = Pattern.compile("\\d+");
                            Matcher ms = ps.matcher(title);
                            ms.find();
                            mMap.put("index", ms.group());
                            listArray.put(mMap);
                        }
                    }
                    list.add(listObject);
                }
                assert cursor != null;
                cursor.close();
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jsonObject = list.get(i);
                    List<JSONObject> newChildList = new ArrayList<>();
                    for (int j = 0; j < listArray.length(); j++) {
                        JSONObject childObject = JSONObject.fromObject(listArray.get(j));
                        if (childObject.getString("path").contains(jsonObject.getString("title"))) {
                            newChildList.add(childObject);
                        }
                    }
                    newChildList = ListSort(newChildList);
                    jsonObject.put("child", GsonUtils.toJson(newChildList));
                }
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        thread.start();
    }

    public static List<JSONObject> ListSort(List<JSONObject> objectList) {
        List<JSONObject> sortedList = new ArrayList<>();
        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < objectList.size(); i++) {
            jsonValues.add(objectList.get(i));
        }
        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            // You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "index";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                Integer valA = null;
                Integer valB = null;
                try {
                    // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                    String aStr = a.getString(KEY_NAME);
                    valA = Integer.parseInt(aStr.replaceAll("-", ""));
                    String bStr = b.getString(KEY_NAME);
                    valB = Integer.parseInt(bStr.replaceAll("-", ""));
                } catch (JSONException e) {
                    // do something
                }
                return -valB.compareTo(valA);
                // if you want to change the sort order, simply use the following:
                // return -valA.compareTo(valB);
            }
        });
        for (int i = 0; i < objectList.size(); i++) {
            sortedList.add(jsonValues.get(i));
        }
        return sortedList;
    }

    private void GetDetailList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO || Environment.getExternalStorageDirectory().getPath() + "/shumei.txt"
                File file = null;
                try {
                    file = new File(list.get(0).getString("path"));

                    if (!file.exists()) {
                        return;
                    }
                    FileInputStream fis;
                    final String RE = "([第].{1,5}[章])(.+)";
                    try {
                        fis = new FileInputStream(file);
                        BufferedReader dr = new BufferedReader(
                                new InputStreamReader(fis, "GBK"));
                        String line = null;
                        long offset = 0;
                        while ((line = dr.readLine()) != null) {
                            if (line.trim().matches(RE)) {
//                                                        Log.e(offset + "", line.trim());
                            }
                            offset = offset + line.length();
                        }
                        dr.close();
                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {
            // setLanguage设置语言
            int result = mTextToSpeech.setLanguage(Locale.CHINESE);
            // TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失
            // TextToSpeech.LANG_NOT_SUPPORTED：不支持
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getContext(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.layout_toolbar)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_toolbar:
                break;
        }
    }

    private void speech(String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            File file = new File(filePath);
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                BufferedReader dr = new BufferedReader(new InputStreamReader(fis));
                String line = null;
                while ((line = dr.readLine()) != null) {
                    if (!line.contains("网址") || !line.contains("首发域名")) {
                        mTextToSpeech.speak(line, isSelect ? TextToSpeech.QUEUE_FLUSH : TextToSpeech.QUEUE_ADD, null, "UniqueID");
                        isSelect = false;
                    }
                }
                dr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        // 不管是否正在朗读TTS都被打断
//        mTextToSpeech.stop();
//        // 关闭，释放资源
//        mTextToSpeech.shutdown();
    }

    @Override
    public void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            mTextToSpeech = null;
        }
        super.onDestroy();
    }
}

package com.blizzard.war.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.entry.ReadPlayEntry;
import com.blizzard.war.utils.BaiduTTS;
import com.blizzard.war.utils.FileUtil;
import com.blizzard.war.utils.GsonUtils;
import com.blizzard.war.utils.LogUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.blizzard.war.utils.Config.READ_LIST_DOWNLOAD_CONTINUE;

public class ReadDownLoadIntentService extends IntentService {
    private List<ReadPlayEntry> nameList = new ArrayList<>();
    private BaiduTTS baiduTTS;
    private String parentName;
    private int loadNum = 0;
    private int downLoadNum = 0;
    private float subNum = 0;

    public ReadDownLoadIntentService() {
        super("com.blizzard.war.ReadDownLoadIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File file = null;
                File audioFile;
                List<ReadEntry> txtPath = GsonUtils.fromJson(intent.getStringExtra("txtPath"), new TypeToken<List<ReadEntry>>() {
                }.getType());
//                if (intent.getIntExtra("type", 0) == 0) {
                for (int j = 0; j < txtPath.size(); j++) {
                    file = new File(txtPath.get(j).getPath());
                    String audioPath = file.getParent() + "/AudioEntry/";
                    audioFile = new File(audioPath);
                    if (!audioFile.exists()) {
                        audioFile.mkdir();
                    }

                    String[] newStr = new String[0];
                    try {
                        newStr = FileUtil.readFileAsString(txtPath.get(j).getPath()).split("。");
                        for (int i = 0; i < newStr.length; i++) {
                            if (!newStr[i].contains("网址") || !newStr[i].contains("首发域名")) {
                                ReadPlayEntry readPlayEntry = new ReadPlayEntry();
                                readPlayEntry.setPath(audioPath + file.getName() + i + ".mp3");
                                readPlayEntry.setContent(newStr[i]);
                                nameList.add(readPlayEntry);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                parentName = new File(file.getParent()).getName() + ".txt";
                downLoadNum = nameList.size();
//                } else {
//                    parentName = new File(new File(intent.getStringExtra("txtPath")).getParent()).getName() + ".txt";
//                    List<ReadPlayEntry> list = null;
//                    if (FileUtil.fileIsExists(parentName)) {
//                        list = GsonUtils.fromJson(FileUtil.LoadFile(getApplicationContext(), parentName), new TypeToken<List<ReadPlayEntry>>() {
//                        }.getType());
//                    } else {
//                        list = GsonUtils.fromJson(intent.getStringExtra("txtPath"), new TypeToken<List<ReadEntry>>() {
//                        }.getType());
//                    }
//                    if (list != null) {
//                        for (int i = 0; i < list.size(); i++) {
//                            ReadPlayEntry readPlayEntity = list.get(i);
//                            if (!FileUtil.fileIsExists(readPlayEntity.getPath())) {
//                                nameList.add(readPlayEntity);
//                                System.out.println(readPlayEntity);
//                            }
//                        }
//                    }
//                    downLoadNum = list.size();
//                    subNum = (list.size() - nameList.size()) / downLoadNum * 100;
//                }

                baiduTTS = new BaiduTTS();
//                baiduTTS.process("这一次交锋，双方都没有占到什么便宜，可以说是旗鼓相当。", Environment.getExternalStorageDirectory().getPath() + File.separator + "/123.mp3");

                changeReadToVoice();
            }
        });
        thread.start();
    }

    private void changeReadToVoice() {
        if (!nameList.get(loadNum).getComplete()) {
            baiduTTS.process(nameList.get(loadNum).getContent(), nameList.get(loadNum).getPath());
        }
        float pressent = (float) loadNum / downLoadNum * 100 + subNum;
        EventBus.getDefault().post(new MessageWrap(new DecimalFormat("#.##").format(pressent), READ_LIST_DOWNLOAD_CONTINUE));
        if ((loadNum + 1) < nameList.size()) {
            loadNum += 1;
            changeReadToVoice();
        } else {
            if (baiduTTS != null) {
                EventBus.getDefault().post(new MessageWrap(new DecimalFormat("#.##").format(100), READ_LIST_DOWNLOAD_CONTINUE));
                baiduTTS.shutdown();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

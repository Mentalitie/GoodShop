package com.blizzard.war.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.blizzard.war.entry.ReadEntry;
import com.blizzard.war.entry.ReadListEntry;
import com.blizzard.war.utils.FileUtil;
import com.blizzard.war.utils.GsonUtils;

import net.sf.json.JSONException;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.blizzard.war.utils.Config.READ_LIST;

public class ReadIntentService extends IntentService {
    private String systemPath;

    public ReadIntentService() {
        super("com.blizzard.war.ReadIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("ReadIntentService 启动");
                /**要实现的方法*/
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), null, "mime_type=\"text/plain\"", null, MediaStore.Files.FileColumns.TITLE);
                File[] file = new File(systemPath + "Story/").listFiles();
                List<ReadEntry> readList = new ArrayList<>();
                ReadListEntry childList = new ReadListEntry();

                for (int i = 0; i < file.length; i++) {
                    ReadEntry readEntry = new ReadEntry();
                    readEntry.setPath(systemPath + "Story/" + file[i].getName());
                    readEntry.setTitle(file[i].getName());

                    while (cursor.moveToNext()) {
                        ReadEntry childRead = new ReadEntry();
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                        if (path.contains(systemPath + "Story/")) {
                            childRead.setPath(path);
                            childRead.setTitle(title);
                            Pattern ps = Pattern.compile("\\d+");
                            Matcher ms = ps.matcher(title);
                            ms.find();
                            childRead.setIndex(ms.group());
                            childList.add(childRead);
                        }
                    }
                    readList.add(readEntry);
                }
                for (int i = 0; i < readList.size(); i++) {
                    ReadEntry readEntry = readList.get(i);
                    List<ReadEntry> newChildList = new ArrayList<>();
                    for (int j = 0; j < childList.getList().size(); j++) {
                        ReadEntry childObject = childList.getList().get(j);
                        if (childObject.getPath().contains(readEntry.getTitle())) {
                            newChildList.add(childObject);
                        }
                    }
                    newChildList = ListSort(newChildList);
                    readEntry.setChild(GsonUtils.toJson(newChildList));
                }
                cursor.close();
                FileUtil.SaveFile(getApplicationContext(), "readList.txt", readList);
                EventBus.getDefault().post(new MessageWrap(readList, READ_LIST));
            }
        });
        thread.start();
    }


    private List<ReadEntry> ListSort(List<ReadEntry> objectList) {
        List<ReadEntry> sortedList = new ArrayList<>();
        List<ReadEntry> jsonValues = new ArrayList<>();
        for (int i = 0; i < objectList.size(); i++) {
            jsonValues.add(objectList.get(i));
        }
        Collections.sort(jsonValues, new Comparator<ReadEntry>() {
            // You can change "Name" with "ID" if you want to sort by ID

            @Override
            public int compare(ReadEntry a, ReadEntry b) {
                Integer valA = null;
                Integer valB = null;
                try {
                    // 这里是a、b需要处理的业务，需要根据你的规则进行修改。
                    String aStr = a.getIndex();
                    valA = Integer.parseInt(aStr.replaceAll("-", ""));
                    String bStr = b.getIndex();
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

    @Override
    public void onCreate() {
        super.onCreate();
        systemPath = Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

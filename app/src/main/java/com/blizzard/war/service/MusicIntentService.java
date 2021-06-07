package com.blizzard.war.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.blizzard.war.app.BiliApplication;
import com.blizzard.war.entry.AudioEntry;
import com.blizzard.war.entry.AudioListEntry;
import com.blizzard.war.utils.FileUtil;

import net.sf.json.JSONException;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import static com.blizzard.war.utils.Config.MUSIC_LIST;

public class MusicIntentService extends IntentService {

    public MusicIntentService() {
        super("com.blizzard.war.MusicIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                /**要实现的方法*/
                ContentResolver contentResolver = BiliApplication.GetContext().getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  // 这个Uri代表要查询的数据库名称加上表的名称。
                String[] projection = null; // 这个参数代表要从表中选择的列，用一个String数组来表示。
                String selection = null;    // 相当于SQL语句中的where子句，就是代表你的查询条件。
                String[] selectionArgs = null;  // 这个参数是说你的Selections里有？这个符号是，这里可以以实际值代替这个问号。如果Selections这个没有？的话，那么这个String数组可以为null。
                String sortOrder = MediaStore.Audio.Media.ARTIST_ID;   // 排序
                Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
                AudioListEntry audioListEntry = new AudioListEntry();

                while (cursor.moveToNext()) {
                    try {
                        AudioEntry audioEntry = new AudioEntry();
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));    // 获取音频时长
                        int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));    // 获取音频文件大小
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));   // 获取音频ID
                        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));   // 获取音频地址
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));    // 获取音频名称
                        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));    // 获取音频专辑名
                        String year = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));    // 获取音频发行日期
                        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));   // 获取音频歌手名
                        String display_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replaceAll(" ", "");  // 获取音频文件的名称
                        String song_name = new File(url).getName();

                        if (song_name.contains("-")) {
                            song_name = song_name.substring(0, song_name.lastIndexOf("."));
                            if (song_name.contains(" -")) {
                                song_name = song_name.substring(0, song_name.indexOf(" -")) + " - " + song_name.substring(song_name.indexOf("- ") + 2);
                            } else {
                                song_name = song_name.substring(0, song_name.indexOf("-")) + " - " + song_name.substring(song_name.indexOf("-") + 1);
                            }
                        } else {
                            song_name = artist + " - ";
                            if (title.lastIndexOf("-") == -1) {
                                song_name += title;
                            } else {
                                song_name += title.substring(0, title.lastIndexOf("-"));
                            }
                        }

                        audioEntry.setDuration(duration);
                        audioEntry.setSize(size);
                        audioEntry.setId(id);
                        audioEntry.setUrl(url);
                        audioEntry.setTitle(title);
                        audioEntry.setAlbum(album);
                        audioEntry.setYear(year);
                        audioEntry.setArtist(artist);
                        audioEntry.setDisplay_name(display_name);
                        audioEntry.setSong_name(song_name);

                        if (duration / 1000 > 30) {
                            audioListEntry.add(audioEntry);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
                FileUtil.SaveFile(getApplicationContext(), "musicList.txt", audioListEntry.getList());

                EventBus.getDefault().post(new MessageWrap(audioListEntry.getList(), MUSIC_LIST));
            }
        });
        thread.start();
    }
}

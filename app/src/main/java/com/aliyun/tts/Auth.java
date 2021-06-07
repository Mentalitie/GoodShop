package com.aliyun.tts;

import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blizzard.war.mvp.ui.activity.token.AccessToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class Auth {
    private static final String appKey = "EXI91JMEXmg2ioFM";

    // 将鉴权信息打包成json格式
    public static JSONObject getTicket() {
        JSONObject object = new JSONObject();
        object.put("ak_id", "LTAI5tGwwepFKwTBs4HkSMda");                          // 设置阿里云账号信息
        object.put("ak_secret", "7I4oC0nBGPCHirjCaVxrYSJcdezEsW");           // 设置阿里云账号信息
        object.put("app_key", appKey);             // 设置阿里云账号信息
        object.put("device_id", appKey);                      // 设置设备唯一码 android.os.Build.SERIAL
        object.put("sdk_code", "software_nls_tts_offline");  // 设为离线合成 精品：software_nls_tts_offline 标准：software_nls_tts_offline_standard
        object.put("mode_type", "0");
        return object;
    }

    // 也可以将鉴权信息以json格式保存至文件，然后从文件里加载（必须包含成员：ak_id/ak_secret/app_key/device_id/sdk_code）
    // 该方式切换账号切换账号比较方便
    public static JSONObject getTicketFromJsonFile(String fileName) {
        try {
            String jsonStr = "";
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return JSON.parseObject(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject getAliYunTicket() {
        JSONObject object = new JSONObject();
        final AccessToken token;
        //From Aliyun 请根据相关文档获取并填入
        String app_key = "";
        String accessKeyId = "";
        String accessKeySecret = "";


        token = new AccessToken(accessKeyId, accessKeySecret);
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    token.apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        th.start();
        try {
            th.join(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String token_txt = token.getToken();
        long expired_time = token.getExpireTime();

        object.put("app_key", app_key);
        object.put("token", token_txt);
        object.put("device_id", appKey);
        return object;
    }
}

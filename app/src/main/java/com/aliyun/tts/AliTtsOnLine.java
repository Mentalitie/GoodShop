package com.aliyun.tts;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.nui.CommonUtils;
import com.alibaba.idst.nui.Constants;
import com.alibaba.idst.nui.INativeTtsCallback;
import com.alibaba.idst.nui.NativeNui;
import com.blizzard.war.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AliTtsOnLine {
    private final String TAG = "BAIDUTTS";
    private NativeNui nui_tts_instance = new NativeNui(Constants.ModeType.MODE_TTS);
    private String asset_path;
    private boolean mPlaying = false;  // 用于管控AudioPlaying的播放状态
    private String mFontName = "aiqi";
    private Activity mActivity;
    private String authTxt;
    private PlayOver mPlayOver;

    public AliTtsOnLine(Activity activity) {
        mActivity = activity;
        // 拷贝资源
        CommonUtils.copyAssetsData(activity);
        String path = CommonUtils.getModelPath(activity);
        Log.e(TAG, "workpath = " + path);
        asset_path = path + "/";
        readFileByBytes();
        initPage(asset_path);
    }

    public void play(String text) {
        nui_tts_instance.startTts("1", "", text);
    }

    public void stop(){
        mAudioTrack.stop();
        nui_tts_instance.tts_release();
    }

    // 播报模块示例：展示合成音频播放
    // AudioPlayer默认采样率是16000
    private AudioPlayer mAudioTrack = new AudioPlayer(new AudioPlayerCallback() {
        @Override
        public void playStart() {
        }

        @Override
        public void playOver() {
            if (mPlayOver != null) {
                try {
                    Thread.sleep(1000);
                    mPlayOver.over();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    public interface PlayOver {
        void over();
    }

    public void setListen(PlayOver playOver) {
        mPlayOver = playOver;
    }


    private void initPage(String path) {
        // SDK初始化
        genInitParams(path);
        // 注意：离线合成暂不支持多实例
        int ret = nui_tts_instance.tts_initialize(new INativeTtsCallback() {
            public void onTtsEventCallback(INativeTtsCallback.TtsEvent event, String task_id, int ret_code) {
                Log.i(TAG, "event:" + event + " task id " + task_id + " ret " + ret_code);
                if (event == INativeTtsCallback.TtsEvent.TTS_EVENT_START) {
                    Log.i(TAG, "eventStart");
                    mAudioTrack.play();
                } else if (event == INativeTtsCallback.TtsEvent.TTS_EVENT_END || event == TtsEvent.TTS_EVENT_CANCEL || event == TtsEvent.TTS_EVENT_ERROR) {
                    Log.i(TAG, "eventEnd");
                    mAudioTrack.isFinishSend(true);
                } else if (event == TtsEvent.TTS_EVENT_PAUSE) {
                    mAudioTrack.pause();
                    Log.i(TAG, "eventPause");
                } else if (event == TtsEvent.TTS_EVENT_RESUME) {
                    mAudioTrack.play();
                }
            }

            @Override
            public void onTtsDataCallback(String info, int info_len, byte[] data) {
                if (info.length() > 0) {
                    Log.i(TAG, "info: " + info);
                }
                if (data.length > 0) {
                    mAudioTrack.setAudioData(data);
                }
            }

            @Override
            public void onTtsVolCallback(int volume) {
                Log.i(TAG, "volume " + volume);
            }
        }, authTxt, Constants.LogLevel.LOG_LEVEL_VERBOSE, true);
        if (Constants.NuiResultCode.SUCCESS == ret) {
            // 初始化成功
            Log.e(TAG, "初始化成功");
        } else {
            // 初始化失败，通过“error_msg”查看详细错误信息，离线语音合成FAQ文档中已列出常见错误
            String errmsg = nui_tts_instance.getparamTts("error_msg");
            Log.e(TAG, "初始化失败" + errmsg);
            // 初始化失败时直接返回，不用再调用合成或者参数设置接口
        }
        // 语音包和SDK是隔离的，需要先设置语音包
        // 如果切换发音人：SDK可使用语音包与鉴权账号相关，由购买时获得的语音包使用权限决定
        // 如已经购买xiaoyun，按下边方式调用后，发音人将切为xiaoyun
        // 语音包下载地址：https://help.aliyun.com/document_detail/204185.html?spm=a2c4g.11186623.6.628.3cde73409gZCmA
        String fullName = asset_path + mFontName;
        if (!isExist(fullName)) {
            Log.e(TAG, fullName + " does not exist");
        }
        ret = nui_tts_instance.setparamTts("extend_font_name", fullName);

        if (Constants.NuiResultCode.SUCCESS != ret) {
            Log.e(TAG, "failed to setfont:" + ret);
            ToastUtil.show("设置发音人失败，错误码:" + ret);
        }
        UpdateAudioPlayerSampleRate();
//        // 调整语速
        nui_tts_instance.setparamTts("speed_level", "1");
//        // 调整音调
        nui_tts_instance.setparamTts("pitch_level", "0");
//        // 调整音量
        nui_tts_instance.setparamTts("volume", "1.0");
    }


    private boolean isExist(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            ToastUtil.show("打不开：" + filename);
            return false;
        } else {
            return true;
        }
    }

    private void genInitParams(String workpath) {
        String str = "";
        try {
            // 需要特别注意：ak_id/ak_secret/app_key/sdk_code/device_id等参数必须传入SDK
            // 离线语音合成sdk_code的值为：software_nls_tts_offline
            // 鉴权信息获取参：https://help.aliyun.com/document_detail/204186.html?spm=a2c4g.11186623.6.631.1ec738ba1L99j6#title-n2u-kqh-7ul
            JSONObject initObject = Auth.getTicket();
            String ak_secret = initObject.getString("ak_secret");
            // 如果没有通过函数设置鉴权信息，尝试从本地文件读取
            if (ak_secret.equals("")) {
                // 如果接口没有设置鉴权信息，尝试从本地鉴权文件加载（方便测试人员多账号验证）
                initObject = null;
                String fileName = asset_path + "auth.txt";
                if (isExist(fileName)) {
                    Log.i(TAG, "get ticket from json file:" + fileName);
                    initObject = Auth.getTicketFromJsonFile(fileName);
                }
                if (initObject == null) {
                    ToastUtil.show("无法获取有效鉴权信息，请尝试在demo通过代码设置或者将鉴权信息以json格式保存至本地文件(/sdcard/idst/auth.txt)");
                }
            }
            initObject.put("workspace", workpath);
            // 默认为在线合成，这里切为离线合成
            initObject.put("mode_type", "0");
            str = initObject.toString();

            // 为方便调试，将鉴权文件生成至固定目录（如“/storage/emulated/0/tts”），避免卸载app重装时再次鉴权，主要用于接入时调试。不设置该参数时，鉴权文件将存在workspace目录下。
            saveFile(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "ticket:" + str);
        authTxt = str;
    }


    private void UpdateAudioPlayerSampleRate() {
        // 获取当前模型采样率
        String samplerate_s = nui_tts_instance.getparamTts("model_sample_rate");
        if (samplerate_s != null) {
            mAudioTrack.setSampleRate(Integer.parseInt(samplerate_s));
        }
    }

    private void readFileByBytes() {
        try {
            InputStream is = mActivity.getAssets().open(mFontName);
            FileOutputStream fos = new FileOutputStream(new File(asset_path + mFontName));
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(String str) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(asset_path + "auth.txt"));
            fos.write(str.getBytes());// 将读取的输入流写入到输出流
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

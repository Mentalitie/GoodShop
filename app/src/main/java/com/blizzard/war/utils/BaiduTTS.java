package com.blizzard.war.utils;
//
//import com.alibaba.nls.client.AccessToken;
//import com.alibaba.nls.client.protocol.NlsClient;
//import com.alibaba.nls.client.protocol.OutputFormatEnum;
//import com.alibaba.nls.client.protocol.SampleRateEnum;
//import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
//import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
//import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
//public class BaiduTTS {
//    private static final Logger logger = LoggerFactory.getLogger(BaiduTTS.class);
//    private static long startTime;
//    private String appKey;
//    private String token;
//    private String url = "";
//    private File mAudioFile;
//    private static BaiduTTSListener mBaiduTTSListener;
//    private FileOutputStream fout;
//    private SpeechSynthesizer synthesizer;
//    private boolean firstRecvBinary = true;
//
//    NlsClient client;
//
//    public BaiduTTS() {
//        AccessToken accessToken = new AccessToken("LTAI5tGwwepFKwTBs4HkSMda", "7I4oC0nBGPCHirjCaVxrYSJcdezEsW");
//        try {
//            accessToken.apply();
//            this.appKey = "N9Sl4PTxZkRP6PXd";
//            this.token = accessToken.getToken();
//            this.url = ""; // 默认即可，默认值：wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //TODO 重要提示 创建NlsClient实例,应用全局创建一个即可,生命周期可和整个应用保持一致,默认服务地址为阿里云线上服务地址
//        if (url.isEmpty()) {
//            client = new NlsClient(token);
//        } else {
//            client = new NlsClient(url, token);
//        }
//    }
//
//
//    private SpeechSynthesizerListener getSynthesizerListener(String path) {
//        SpeechSynthesizerListener listener = null;
//        try {
//            mAudioFile = new File(path);
//            fout = new FileOutputStream(mAudioFile);
//            listener = new SpeechSynthesizerListener() {
//
//                //语音合成结束
//                @Override
//                public void onComplete(SpeechSynthesizerResponse response) {
//                    // TODO 当onComplete时表示所有TTS数据已经接收完成，因此这个是整个合成延迟，该延迟可能较大，未必满足实时场景
//                    System.out.println("name: " + response.getName() + ", status: " + response.getStatus() + ", output file :" + mAudioFile.getAbsolutePath());
//                    mBaiduTTSListener.isComplete();
//                }
//
//                //语音合成的语音二进制数据
//                @Override
//                public void onMessage(ByteBuffer message) {
//                    try {
//                        byte[] bytesArray = new byte[message.remaining()];
//                        message.get(bytesArray, 0, bytesArray.length);
////                        System.out.println("write array:" + bytesArray.length);
//                        fout.write(bytesArray);
//                        if (firstRecvBinary) {
//                            // TODO 此处是计算首包语音流的延迟，收到第一包语音流时，即可以进行语音播放，以提升响应速度(特别是实时交互场景下)
//                            firstRecvBinary = false;
//                            long now = System.currentTimeMillis();
////                            logger.info("tts first latency : " + (now - BaiduTTS.startTime) + " ms");
//
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFail(SpeechSynthesizerResponse response) {
//                    // TODO 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
//                    System.out.println(
//                            "task_id: " + response.getTaskId() +
//                                    //状态码 20000000 表示识别成功
//                                    ", status: " + response.getStatus() +
//                                    //错误信息
//                                    ", status_text: " + response.getStatusText());
//                }
//
//                @Override
//                public void onMetaInfo(SpeechSynthesizerResponse response) {
//                    System.out.println("MetaInfo event:{}" + response.getTaskId());
//                }
//            };
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return listener;
//    }
//
//    public void process(String str, String path) {
//        synthesizer = null;
//        try {
//            //创建实例,建立连接
//            synthesizer = new SpeechSynthesizer(client, getSynthesizerListener(path));
//            synthesizer.setAppKey(appKey);
//            //设置返回音频的编码格式
//            synthesizer.setFormat(OutputFormatEnum.MP3);
//            //设置返回音频的采样率
//            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
//            //发音人
//            synthesizer.setVoice("ruoxi");
//            //语调，范围是-500~500，可选，默认是0
//            synthesizer.setPitchRate(0);
//            //语速，范围是-500~500，默认是0
//            synthesizer.setSpeechRate(20);
//            //音量，范围是0~100，可选，默认50
//            synthesizer.setVolume(100);
//            //设置用于语音合成的文本
//            synthesizer.setText(str);
//            // 是否开启字幕功能（返回对应文本的相应时间戳），默认不开启。
//            synthesizer.addCustomedParam("enable_subtitle", false);
//
//            //此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
//            long start = System.currentTimeMillis();
//            synthesizer.start();
////            logger.info("tts start latency " + (System.currentTimeMillis() - start) + " ms");
//
//            BaiduTTS.startTime = System.currentTimeMillis();
//
//            //等待语音合成结束
//            synthesizer.waitForComplete();
////            logger.info("tts stop latency " + (System.currentTimeMillis() - start) + " ms");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //关闭连接
//            if (null != synthesizer) {
//                synthesizer.close();
//            }
//        }
//    }
//
//    public void shutdown() {
//        client.shutdown();
//    }
//
//    public interface BaiduTTSListener {
//        void isComplete();
//
//        void isShutDown();
//
//    }
//
//    public static void setBaiduTTSListener(BaiduTTSListener baiduTTSListener) {
//        mBaiduTTSListener = baiduTTSListener;
//    }
//
//}

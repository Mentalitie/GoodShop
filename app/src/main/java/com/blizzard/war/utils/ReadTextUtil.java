package com.blizzard.war.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * 功能描述:
 * 读取txt文件
 *
 * @auther: ma
 * @param: ReadTextUtil
 * @date: 2021-04-27 02:38
 */
public class ReadTextUtil {

    private RandomAccessFile readFile;
    private long pages = 0;//总页数
    private int pageSize;//每一页的字节数 字节数固定
    private long bytesCount;//字节总数
    private boolean isFile = true;
    private String format = "utf-8";

    private String filePath;


    /**
     * @param filePath 文件路径
     * @return
     * @method
     * @description
     * @date: 2020/3/31 9:27
     */
    public ReadTextUtil(String filePath) {
        this.filePath = filePath;
        readText();
    }

    private void readText() {
        File file = new File(filePath);
        try {
            readFile = new RandomAccessFile(file, "r");
            isFile = true;
            try {
                bytesCount = readFile.length();//获得字节总数
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e("ReadTextUtil:获取字节数失败");
            }
            LogUtil.e("ReadTextUtil文件总字节数" + bytesCount);
            pages = (long) Math.ceil(bytesCount / (float) pageSize);//计算得出文本的页数
            LogUtil.e("ReadTextUtil文件页数" + pages);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            isFile = false;
            LogUtil.e("ReadTextUtil:未发现文件");
        }
    }

    public long getPages() {
        return pages;
    }

    public void refresh() {
        close();
        readText();
    }

    //上一页功能的实现
    public String getText() {
        String content = "读取异常";
        //第一页 的情况 定位在0字节处 读取内容 当前页数不改变
        seek(0);
        try {
            content = read(format);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void close() {
        try {
            readFile.close();
            readFile = null;
        } catch (IOException e) {
            e.printStackTrace();
            readFile = null;
        }
    }

    //定位字节位置 根据页数定位文本的位置
    //page   1到最后一页
    private void seek(long page) {
        try {
            //if(pages)
            readFile.seek(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String read(String format) throws IOException {
        //内容重叠30 防止末尾字节乱码
        byte[] chs = new byte[(int) bytesCount];
        readFile.read(chs);
        return new String(chs);
    }

    public String readTxt() {
        File file = new File(filePath);
        FileInputStream fis;
        String str = "";
        try {
            fis = new FileInputStream(file);
            BufferedReader dr = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = dr.readLine()) != null) {
                str += line;
            }
            dr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }
}

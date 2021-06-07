package com.blizzard.war.utils;

import android.content.Context;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读取工具类
 */
public class FileUtil {

    private static void errorMsg(File file, String filePath) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        }
    }

    public static List<String> readFileAsLine(String filePath) throws IOException {
        File file = new File(filePath);
        List<String> list = new ArrayList<>();
        errorMsg(file, filePath);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            BufferedReader dr = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = dr.readLine()) != null) {
                if (!line.contains("网址") || !line.contains("首发域名")) {
                    list.add(line);
                }
            }
            dr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        errorMsg(file, filePath);

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流  
        FileInputStream fis = new FileInputStream(file);
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];
        // 用于保存实际读取的字节数
        int hasRead = 0;
        while ((hasRead = fis.read(bbuf)) > 0) {
            sb.append(new String(bbuf, 0, hasRead));
        }
        fis.close();
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }

    public static void SaveFile(Context context, String fileName, List<?> list) {
        SaveFile(context, fileName, GsonUtils.toJson(list));
    }

    public static void SaveFile(Context context, String fileName, String str) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String LoadFile(Context context, String path) {
        String str = null;
        try {
            FileInputStream f = context.openFileInput(path);
            StringBuilder sb = new StringBuilder();
            // 创建字节输入流
            // 创建一个长度为10240的Buffer
            byte[] bbuf = new byte[f.available()];
            // 用于保存实际读取的字节数
            int hasRead = 0;
            while ((hasRead = f.read(bbuf)) > 0) {
                sb.append(new String(bbuf, 0, hasRead));
            }
            f.close();
            str = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

}

package com.blizzard.war.utils;

/**
 * 功能描述:
 * 金额转换
 *
 * @auther: ma
 * @param: NumberUtil
 * @date: 2019/4/16 11:18
 */
public class NumberUtil {
    public static String converString(int num) {
        if (num < 100000) {
            return String.valueOf(num);
        }
        String unit = "万";
        double newNum = num / 10000.0;
        String numStr = String.format("%." + 1 + "f", newNum);
        return numStr + unit;
    }
}

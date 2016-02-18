package com.mingchaogui.twiggle.util;


public class NumberUtil {

    public static String toScreenText(String text, long num) {
        String numText;
        if (num > 100000) {
            long wanCount = num / 10000;
            numText = wanCount + "万";
        } else {
            numText = String.valueOf(num);
        }

        return String.format(text, numText);
    }
}

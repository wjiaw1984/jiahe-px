package com.jiahe.px.common.core.utils;

/**
 * Created by wzm on 2019/2/13.
 */
public class StringUtils {
    public static String lowerFirst(String oldStr) {
        char[] chars = oldStr.toCharArray();

        chars[0] += 32;

        return String.valueOf(chars);
    }

    public static String replaceComplete(String str, String oldStr, String newStr){
        if(!org.springframework.util.StringUtils.hasText(str)
                || !org.springframework.util.StringUtils.hasText(oldStr)
                || newStr == null){
            return str;
        }

        int idx = str.indexOf(oldStr);
        if(idx == -1){
            return str;
        }

        StringBuilder stringBuilder = new StringBuilder();
        while(true){
            idx = str.indexOf(oldStr);
            if(idx == -1){
                break;
            }

            int preIdx = idx - 1;
            String ch = "";

            if(preIdx >= 0){
                ch = String.valueOf(str.charAt(preIdx));
            }

            if(ch != "" || ch != " "){
                continue;
            }

        }
        return "";
    }
}
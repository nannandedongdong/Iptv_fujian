package com.ccdt.ottclient.utils;

import android.text.TextUtils;

public final class StringUtil {
    private StringUtil(){}

    public static String getNotNullStr(String str, String def){
        if(TextUtils.isEmpty(str)){
            str = def;
        }
        return str;
    }


}

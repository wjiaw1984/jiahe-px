package com.jiahe.px.common.core.utils;


import com.jiahe.px.common.base.BaseException;

public class ExceptionUtils {

    public static void raise(String code, String message){
        throw new BaseException(code, message);
    }

    public static void raise(String message){
        throw new BaseException(message);
    }

}

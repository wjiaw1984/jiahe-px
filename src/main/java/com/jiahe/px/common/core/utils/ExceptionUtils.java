package com.jiahe.px.common.core.utils;

import com.efuture.myshop.common.base.BaseException;

/**
 * Created by wzm on 2018/8/24.
 */
public class ExceptionUtils {

    public static void raise(String code, String message){
        throw new BaseException(code, message);
    }

    public static void raise(String message){
        throw new BaseException(message);
    }

}

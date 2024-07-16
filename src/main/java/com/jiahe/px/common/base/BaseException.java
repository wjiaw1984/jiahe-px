package com.jiahe.px.common.base;

import lombok.Data;

import java.text.MessageFormat;

@Data
public class BaseException extends RuntimeException{
    private String code;
    private String message;
    private Object tag;

    public BaseException(){

    }

    public BaseException(String message){
        this("-1", message, null);
    }

    public BaseException(String code, String message){
        this(code, message, null);
    }

    public BaseException(String code, String message, Throwable cause){
        super(MessageFormat.format("code:{0},msg:{1}", code, message), cause);

        this.code = code;
        this.message = message;
    }

}

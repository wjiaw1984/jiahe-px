package com.jiahe.px.common.base.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseVO<T> implements Serializable {
    /**
     * 返回编码
     *
     */
    String code;
    /**
     * 返回数据
     */
    T data;

    public static BaseVO<String> buildErrorResponse(String code,String errMsg){
        BaseVO<String> result = new BaseVO<String>();
        result.setData(errMsg);
        result.setCode(code);
        return result;
    }

    public static BaseVO buildErrorResponse(String code,Object entity){
        BaseVO result = new BaseVO(entity);
        result.setCode(code);
        return result;
    }

    public static BaseVO buildSuccess(Object entity){
        BaseVO result = new BaseVO(entity);
        result.setCode("0");
        return result;
    }

    public BaseVO(){

    }
    public BaseVO(T entity){
        data = entity;
    }
}

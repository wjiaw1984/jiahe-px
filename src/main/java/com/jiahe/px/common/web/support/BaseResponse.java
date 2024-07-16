package com.jiahe.px.common.web.support;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
@Slf4j
public class BaseResponse implements Serializable {
    public static String SUCCSS_MSG = "成功";
    public static String FAIL_MSG = "失败";

    private String code;
    private Object data;
    private String msg;

    public static BaseResponse buildSuccess(Object obj)
    {
        BaseResponse response = new BaseResponse();
        response.setCode(ResponseCode.SUCCESS);
        response.setData(obj);
        return response;
    }

    public static BaseResponse buildFailure(String returncode)
    {
        return buildFailure(returncode,null);
    }

    public static BaseResponse buildFailure(String returncode, String msg)
    {
        BaseResponse response = new BaseResponse();
        response.setCode(returncode);
        response.setMsg(StringUtils.hasText(msg)?msg:FAIL_MSG);
        return response;
    }
}

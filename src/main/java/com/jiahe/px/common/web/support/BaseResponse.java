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

    private String returncode;
    private Object data;

    public static BaseResponse buildSuccess(Object obj)
    {
        BaseResponse response = new BaseResponse();
        response.setReturncode(ResponseCode.SUCCESS);
        response.setData(obj);
        return response;
    }

    public static BaseResponse buildFailure(String returncode)
    {
        return buildFailure(returncode,null);
    }

//    public static BaseResponse buildFailure(String returncode, String formatmsg, Object... args)
//    {
//        SessionContext sessionContext = SessionContextHolder.get();
//
//        BaseResponse response = new BaseResponse();
//
//        response.setReturncode(returncode);
//        response.setData(MessageSourceHelper.getDefault().getMessage(returncode, formatmsg,
//                sessionContext != null && sessionContext.getLocale() != null ? new Locale(sessionContext.getLocale()) : null,args));
//        return response;
//    }

    public static BaseResponse buildFailure(String returncode, String msg)
    {
        BaseResponse response = new BaseResponse();
        response.setReturncode(returncode);
        response.setData(StringUtils.hasText(msg)?msg:FAIL_MSG);
        return response;
    }
}

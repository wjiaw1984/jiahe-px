package com.jiahe.px.common.web.support;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
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

    public static BaseResponse buildFailure(RpcException e) {
        BaseResponse response = new BaseResponse();
        response.setReturncode("7000" + String.valueOf(e.getCode()));
        if (e.getCode() == 1) {
            if (e.getMessage().indexOf("请勿重复提交") >= 0) {
                response.setReturncode("9005");
                response.setData("请勿重复提交！");
            } else {
                response.setData("网络响应异常！");
            }
        } else if (e.getCode() == 2) {
            response.setData("服务响应超时！");
        } else if (e.getCode() == 3) {
            response.setData("服务响应超时！");
        } else if (e.getCode() == 4) {
            response.setData("未找到服务响应信息！");
        } else if (e.getCode() == 5) {
            response.setData("序列化异常！");
        } else if (e.getCode() == 6) {
            response.setData("集群异常！");
        } else if (e.getCode() == 7) {
            response.setData("服务流量限制！");
        } else if (e.getCode() == 8) {
            response.setData("没有可用于该服务的提供程序！" + e.getMessage());
        } else if (e.getCode() == 9005) {
            response.setData(e.getMessage());
        }else{
            response.setData("未知异常！");
        }
        return response;
    }
}

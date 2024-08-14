package com.jiahe.px.send.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jiahe.px.common.core.utils.Convert;
import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.config.AppConfig;
import com.jiahe.px.model.YHRequestBase;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.model.order.ReqQueryOrderVo;
import com.jiahe.px.model.order.ReqReceiveVo;
import com.jiahe.px.model.order.ResQueryOrderVo;
import com.jiahe.px.send.HttpInstance;
import com.jiahe.px.send.IHttpBaseCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Date: 2024/7/16
 */
@Slf4j
@Component
public class HttpBaseCallServiceImpl implements IHttpBaseCallService {
    @Autowired
    AppConfig appConfig;

    @Override
    public BaseResponse<ResPxGoodsPriceVo> goodsPriceQuery(ReqPxGoodsPriceVo entity) {
        BaseResponse<ResPxGoodsPriceVo> result = call("goodsPriceQuery", entity);
        JSONObject jsonObject = JSONObject.from(result.getData()) ;
        result.setData(jsonObject.toJavaObject(ResPxGoodsPriceVo.class));
        return result;
    }

    public BaseResponse call(String getFuncName, YHRequestBase entity) {
        Thread th = Thread.currentThread();
        long startTime = System.currentTimeMillis();
        BaseResponse resBody = null;
        Call<BaseResponse> call = null;

        if ("".equals(appConfig.getUrl())) {
            throw new RuntimeException("线程【" + th.getId() + "】推送地址为空！");
        }


        Long timestamp = Convert.getSecondTimestamp();
        entity.setReqTime(timestamp);
        entity.setCustomNo(appConfig.getCustomNo());
        entity.setAppId(appConfig.getAppId());
        entity.buildSign(appConfig.getAppSecret());
        Object[] paramArray;
            paramArray = new Object[]{entity};
        try {
            log.info("线程【" + th.getId() + "】推送开始");
            call = (Call<BaseResponse>) HttpInstance.post(appConfig.getUrl(), getFuncName, paramArray);
            log.info("线程【" + th.getId() + "】推送结束");
        } catch (Exception ex) {
            log.error("线程【" + th.getId() + "】获取远程接口方法【" + appConfig.getUrl() + "/" + getFuncName + "】异常："
                    + ex.getMessage());
            throw new RuntimeException("线程【" + th.getId() + "】获取远程接口方法【" + appConfig.getUrl() + "/" + getFuncName + "】异常："
                    + ex.getMessage());
        }
        try {
            Response<BaseResponse> response;
            try {
                response = call.execute();
            } catch (IOException e) {
                throw new RuntimeException("远程服务异常！");
            }
            if (response.code() == 200) {
                resBody = response.body();
            } else {
                resBody = JSON.parseObject(response.errorBody().string(), BaseResponse.class);
                log.error("线程【" + th.getId() + "】调用远程服务【" + getFuncName + "】异常：" + response.errorBody().string());
            }
            if (resBody.getCode().equals("0")) {
                long endTime = System.currentTimeMillis();
                long executeTime = endTime - startTime;
                log.warn("当前线程【" + th.getId() + "】调用远程服务交互总用时：" + executeTime + "ms");

                return resBody;
            } else {
                //log.error("线程【" + th.getId() + "】调用远程服务获取令牌时异常：" + resBody.getData());
                throw new RuntimeException(Convert.ToString(resBody.getMsg()));
            }

        } catch (Exception ex) {
            log.error("线程【" + th.getId() + "】调用远程服务【" + getFuncName + "】异常：" + ex.getMessage());
            throw new RuntimeException(String.format("远程服务异常: %s",ex.getMessage()));
        }
    }

    @Override
    public BaseResponse orderSave(ReqOrderSaveVo entity) {
        BaseResponse result = call("orderSave", entity);
        return result;
    }

    @Override
    public BaseResponse<ResQueryOrderVo> queryOrder(ReqQueryOrderVo entity) {
        BaseResponse<ResQueryOrderVo> result = call("queryOrder", entity);
        JSONObject jsonObject = JSONObject.from(result.getData()) ;
        result.setData(jsonObject.toJavaObject(ResQueryOrderVo.class));
        return result;
    }

    @Override
    public BaseResponse receive(ReqReceiveVo entity) {
        BaseResponse result = call("receive", entity);
        return result;
    }
}

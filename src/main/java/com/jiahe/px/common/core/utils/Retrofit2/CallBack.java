package com.jiahe.px.common.core.utils.Retrofit2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public abstract class CallBack<T> implements Callback<T> {
    private final static Logger logger = LoggerFactory.getLogger(CallBack.class);

    @Override
    public void onResponse(Call call, Response response) {
        if (200 == response.code()) {
            //对后台返回的数据进行处理
            onSuccessful(call, response);
        } else {
            //对后台返回200～300之间的错误进行处理
            onFail(call, null, response);
        }
    }



    @Override
    public void onFailure(Call call, Throwable t) {
        onFail(call, t, null);
    }

    public abstract void onSuccessful(Call<T> call, Response<T> response);

    protected void onFail(Call<T> call, Throwable t, Response<T> response) {
        if (null == response) {
            logger.error("【" + call.getClass().getName() + "】远程调用无返回信息。");
            return;
        }
        logger.error("【" + call.getClass().getName() + "】RESPONSE code is " + response.code() + ": " + response.raw().toString());
        if (null != response.errorBody()) {
            //解析后台返回的错误信息
            String message="";
            try {
                message = response.errorBody().string();
            } catch (IOException e) {
                logger.error("【" + call.getClass().getName() + "】远程服务异常信息获取异常！" + e.getMessage());
            }
            logger.error("【" + call.getClass().getName() + "】远程服务异常信息：" + message);

            // errorEntity.getErrorCode() 获取后台返回的 errorCode，根据 errorCode 前端做相应的处理
        }
    }
}

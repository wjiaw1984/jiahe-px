package com.jiahe.px.send;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * 永辉批销远程调用
 * Date: 2024/7/16
 */
public interface ISendHTTPTarget {

    @POST("/px/api/v1/thirdparty/price")
    @Headers({"Content-Type:application/json; charset=utf-8","Accept:application/json"})
    Call<BaseResponse> goodsPriceQuery(@Body ReqPxGoodsPriceVo entity);


}

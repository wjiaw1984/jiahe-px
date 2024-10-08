package com.jiahe.px.send;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.model.order.ReqQueryOrderVo;
import com.jiahe.px.model.order.ReqReceiveVo;
import com.jiahe.px.model.order.ResQueryOrderVo;

public interface IHttpBaseCallService {
    BaseResponse<ResPxGoodsPriceVo> goodsPriceQuery(ReqPxGoodsPriceVo entity);
    BaseResponse orderSave(ReqOrderSaveVo entity);
    BaseResponse<ResQueryOrderVo> queryOrder(ReqQueryOrderVo entity);
    BaseResponse receive(ReqReceiveVo entity);
}

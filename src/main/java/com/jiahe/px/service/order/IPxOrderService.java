package com.jiahe.px.service.order;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.model.order.ReqQueryOrderVo;
import com.jiahe.px.model.order.ReqReceiveVo;
import com.jiahe.px.model.order.ResQueryOrderVo;

/**
 * 批销订单处理
 * Date: 2024/7/17
 */
public interface IPxOrderService {
    BaseResponse<ResQueryOrderVo> queryOrder(ReqQueryOrderVo entity);

    BaseResponse orderSave(ReqOrderSaveVo entity);

    BaseResponse receive(ReqReceiveVo entity);
}

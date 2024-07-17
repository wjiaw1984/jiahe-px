package com.jiahe.px.service.order.Impl;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.model.order.ReqQueryOrderVo;
import com.jiahe.px.model.order.ReqReceiveVo;
import com.jiahe.px.model.order.ResQueryOrderVo;
import com.jiahe.px.send.IHttpBaseCallService;
import com.jiahe.px.service.order.IPxOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 批销订单处理
 * Date: 2024/7/17
 */
@Slf4j
@Service("com.jiahe.px.order")
public class PxOrderServiceImpl implements IPxOrderService {
    @Autowired
    IHttpBaseCallService httpBaseCallService;

    @Override
    public BaseResponse<ResQueryOrderVo> queryOrder(ReqQueryOrderVo entity) {
        return httpBaseCallService.queryOrder(entity);
    }

    @Override
    public BaseResponse orderSave(ReqOrderSaveVo entity) {
        return httpBaseCallService.orderSave(entity);
    }

    @Override
    public BaseResponse receive(ReqReceiveVo entity) {
        return httpBaseCallService.receive(entity);
    }
}

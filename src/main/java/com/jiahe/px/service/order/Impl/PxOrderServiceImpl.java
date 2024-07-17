package com.jiahe.px.service.order.Impl;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.model.order.ReqQueryOrderVo;
import com.jiahe.px.model.order.ReqReceiveVo;
import com.jiahe.px.model.order.ResQueryOrderVo;
import com.jiahe.px.mybatis.service.order.IOrderDataService;
import com.jiahe.px.send.IHttpBaseCallService;
import com.jiahe.px.service.order.IPxOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 批销订单处理
 * Date: 2024/7/17
 */
@Slf4j
@Service("com.jiahe.px.order")
public class PxOrderServiceImpl implements IPxOrderService {
    @Autowired
    IHttpBaseCallService httpBaseCallService;

    @Autowired
    IOrderDataService orderDataService;

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

    /*
     * @Description //TODO 推送批销订单
     * @Date 17:22 2024/7/17
     * @Param []
     * @return void
     **/
    @Override
    public void sendOrder() {
        //获取待推送的订单
        List<ReqOrderSaveVo> orderList = orderDataService.listReqOrderSaveByDeliveryStatus("-1");
        if (!CollectionUtils.isEmpty(orderList)){
            //待推送订单处理
            for (ReqOrderSaveVo order : orderList){
                try {
                    httpBaseCallService.orderSave(order);
                    //没有异常的，更新订单状态
                    orderDataService.updateStatus(order.getOrderNo(),"0");
                }catch (Exception e){
                    //发生异常，记录日志信息
                    String errMsg = String.format("推送批销订单异常，订单号：%s，异常信息: %s", order.getOrderNo(), e.getMessage()) ;
                    log.error(errMsg);
                }
            }
        }
    }

    /*
     * @Description //TODO 同步批销订单状态
     * @Date 17:22 2024/7/17
     * @Param []
     * @return void
     **/
    @Override
    public void syncOrder() {

    }
}

package com.jiahe.px.service.order.Impl;

import com.jiahe.px.common.core.utils.Convert;
import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.order.*;
import com.jiahe.px.mybatis.service.order.IOrderDataService;
import com.jiahe.px.send.IHttpBaseCallService;
import com.jiahe.px.service.order.IPxOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
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
        List<String> statues = Arrays.asList("-1");
        List<ReqOrderSaveVo> orderList = orderDataService.listReqOrderSaveByDeliveryStatus(statues);
        if (!CollectionUtils.isEmpty(orderList)) {
            //待推送订单处理
            for (ReqOrderSaveVo order : orderList) {
                try {
                    httpBaseCallService.orderSave(order);
                    //没有异常的，更新订单状态
                    orderDataService.updateStatus(order.getOrderNo(), "0");
                } catch (Exception e) {
                    //发生异常，记录日志信息
                    String errMsg = String.format("推送批销订单异常，订单号：%s，异常信息: %s", order.getOrderNo(), e.getMessage());
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
        //获取待同步的订单
        List<String> statues = Arrays.asList("0");
        List<ReqOrderSaveVo> orderList = orderDataService.listReqOrderSaveByDeliveryStatus(statues);
        if (!CollectionUtils.isEmpty(orderList)) {
            //待同步订单处理
            for (ReqOrderSaveVo order : orderList) {
                try {
                    //调用接口查询订单状态
                    ReqQueryOrderVo entity = new ReqQueryOrderVo();
                    entity.setOrderNo(order.getOrderNo());
                    entity.setShopNo(order.getShopNo());
                    BaseResponse<ResQueryOrderVo> response = httpBaseCallService.queryOrder(entity);
                    if ("0".equals(response.getCode())) {
                        OrderDo orderDo = orderDataService.getOrderByOrderNo(order.getOrderNo());
                        if (orderDo == null
                                || !"0".equals(orderDo.getDeliveryStatus())) {
                            continue;
                        }

                        orderDo.setDeliveryStatus(response.getData().getDeliveryStatus());
                        orderDo.setDeliveryDate(Convert.stringToDate(response.getData().getDeliveryDate(), "yyyy-MM-dd"));
                        orderDataService.updateById(orderDo);

                        List<OrderItemsDo> orderItems = orderDataService.listOrderItemsByOrderNo(order.getOrderNo());
                        if (!CollectionUtils.isEmpty(orderItems)) {
                            for (OrderItemsDo orderItem : orderItems) {
                                OrderItemVo orderItemVo = response.getData().getOrderItem().stream()
                                        .filter(item -> item.getGoodsCode().equals(orderItem.getGoodsCode()))
                                        .findFirst()
                                        .orElse(null);
                                if (orderItemVo == null) {
                                    continue;
                                }
                                if (!orderItem.getDeliveryDate().equals(orderItemVo.getDeliveryStatus())) {
                                    if (!StringUtils.isEmpty(orderItemVo.getDeliveryStatus())) {
                                        orderItem.setDeliveryStatus(orderItemVo.getDeliveryStatus());
                                    }
                                    if (orderItemVo.getDeliveryDate() != null) {
                                        orderItem.setDeliveryDate(orderItemVo.getDeliveryDate());
                                    }

                                    if (!StringUtils.isEmpty(orderItemVo.getDeliveryNo())) {
                                        orderItem.setDeliveryNo(orderItemVo.getDeliveryNo());
                                    }
                                    orderItem.setDeliveryNum(Convert.ToString(orderItemVo.getDeliveryNum().setScale(2)));
                                }
                                orderItem.setPrice(orderItemVo.getPrice().setScale(2));
                            }

                            orderDataService
                                    .updateOrderItems(orderItems);
                        }
                    }
                } catch (Exception e) {
                    //发生异常，记录日志信息
                    String errMsg = String.format("同步更新订单状态信息异常，订单号：%s，异常信息: %s", order.getOrderNo(), e.getMessage());
                    log.error(errMsg);
                }
            }
        }
    }

    /**
     * @return void
     * @Description //TODO 推送验收订单
     * @Date 11:45 2024/7/21
     * @Param []
     **/
    @Override
    public void sendReceipt() {
        List<ReqReceiveVo> orderList = orderDataService.listReqReceiveByDeliveryStatus("-2");
        if (!CollectionUtils.isEmpty(orderList)) {
            //待推送订单处理
            for (ReqReceiveVo order : orderList) {
                try {
                    httpBaseCallService.receive(order);
                    //没有异常的，更新订单状态
                    orderDataService.updateStatus(order.getOrderNo(), "0");
                } catch (Exception e) {
                    //发生异常，记录日志信息
                    String errMsg = String.format("推送批销订单异常，订单号：%s，异常信息: %s", order.getOrderNo(), e.getMessage());
                    log.error(errMsg);
                }
            }
        }
    }
}

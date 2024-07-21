package com.jiahe.px.mybatis.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiahe.px.model.order.OrderDo;
import com.jiahe.px.model.order.OrderItemsDo;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.model.order.ReqReceiveVo;

import java.util.List;

/**
 * 批销订单数据服务
 * Date: 2024/7/17
 */
public interface IOrderDataService extends IService<OrderDo> {
    ReqOrderSaveVo getOrderSaveVo(String orderNo);

    List<OrderDo> listByDeliveryStatus(String deliveryStatus);

    List<ReqOrderSaveVo> listReqOrderSaveByDeliveryStatus(List<String> deliveryStatus);

    void updateStatus(String orderNo, String status);

    List<ReqReceiveVo> listReqReceiveByDeliveryStatus(String deliveryStatus);

    OrderDo getOrderByOrderNo(String orderNo);

    List<OrderItemsDo> listOrderItemsByOrderNo(String orderNo);

    void updateOrderItems(List<OrderItemsDo> orderItemsDos);
}

package com.jiahe.px.model.order;

import lombok.Data;

import java.util.List;

/**
 * 订单查询返回
 * Date: 2024/7/17
 */
@Data
public class ResQueryOrderVo extends Order {
    //订单交货状态  0未交货 1部分交货 2全部交货
    private String deliveryStatus;
    //订单明细
    private List<OrderItemVo> orderItem;
}

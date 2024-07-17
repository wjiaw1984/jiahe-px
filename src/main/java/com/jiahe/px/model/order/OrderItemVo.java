package com.jiahe.px.model.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单查询明细
 * Date: 2024/7/17
 */
@Data
public class OrderItemVo extends OrderItem{
    //交货单号
    private String deliveryNo;
    //交货数量
    private BigDecimal deliveryNum;
    //价格
    private BigDecimal price;
    //交货状态 0未交货 1部分交货 2全部交货
    private String deliveryStatus;
    //交货日期
    private Date deliveryDate;
}

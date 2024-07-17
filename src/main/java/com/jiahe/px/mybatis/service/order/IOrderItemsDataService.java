package com.jiahe.px.mybatis.service.order;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiahe.px.model.order.OrderItemsDo;

import java.util.List;

/**
 * 批销订单明细数据服务
 * Date: 2024/7/17
 */
public interface IOrderItemsDataService extends IService<OrderItemsDo> {
    List<OrderItemsDo> listByOrderNo(String orderNo);

    List<OrderItemsDo> listInOrderNo(List orderNos);
}

package com.jiahe.px.mybatis.service.order.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.model.order.OrderItemsDo;
import com.jiahe.px.mybatis.dao.order.IOrderItemsMapper;
import com.jiahe.px.mybatis.service.order.IOrderItemsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 批销订单明细数据服务
 * Date: 2024/7/17
 */

@Slf4j
@Service
public class OrderItemsDataServiceImpl extends ServiceImpl<IOrderItemsMapper, OrderItemsDo> implements IOrderItemsDataService {
}

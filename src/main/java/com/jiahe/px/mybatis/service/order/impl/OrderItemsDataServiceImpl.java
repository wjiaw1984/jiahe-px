package com.jiahe.px.mybatis.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.model.order.OrderItemsDo;
import com.jiahe.px.mybatis.dao.order.IOrderItemsMapper;
import com.jiahe.px.mybatis.service.order.IOrderItemsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 批销订单明细数据服务
 * Date: 2024/7/17
 */

@Slf4j
@Service
public class OrderItemsDataServiceImpl extends ServiceImpl<IOrderItemsMapper, OrderItemsDo> implements IOrderItemsDataService {
    @Override
    public List<OrderItemsDo> listByOrderNo(String orderNo) {
        QueryWrapper<OrderItemsDo> qw = new QueryWrapper<>();
        qw.eq("orderNo", orderNo);
        return list(qw);
    }

    @Override
    public List<OrderItemsDo> listInOrderNo(List orderNos) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return null;
        }
        QueryWrapper<OrderItemsDo> qw = new QueryWrapper<>();
        qw.in("orderNo", orderNos);
        return list(qw);
    }
}

package com.jiahe.px.mybatis.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.model.order.OrderDo;
import com.jiahe.px.model.order.OrderItem;
import com.jiahe.px.model.order.OrderItemsDo;
import com.jiahe.px.model.order.ReqOrderSaveVo;
import com.jiahe.px.mybatis.dao.order.IOrderMapper;
import com.jiahe.px.mybatis.service.order.IOrderDataService;
import com.jiahe.px.mybatis.service.order.IOrderItemsDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批销订单数据服务
 * Date: 2024/7/17
 */
@Slf4j
@Service
public class OrderDataServiceImpl extends ServiceImpl<IOrderMapper, OrderDo> implements IOrderDataService {

    @Autowired
    IOrderItemsDataService orderItemsDataService;

    @Override
    public ReqOrderSaveVo getOrderSaveVo(String orderNo) {
        ReqOrderSaveVo result = getReqOrderSaveVoByOrderNo(orderNo);
        result.setOrderItem(toOrderItem(orderItemsDataService.listByOrderNo(orderNo)));
        return result;
    }

    public List<OrderItem> toOrderItem(List<OrderItemsDo> orderItemsList) {
        List<OrderItem> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderItemsList)) {
            for (OrderItemsDo item : orderItemsList) {
                result.add(item.toOrderItem());
            }
            log.info("转换成OrderItem对象成功");
        }
        return result;
    }

    public ReqOrderSaveVo getReqOrderSaveVoByOrderNo(String orderNo) {
        OrderDo order = getByOrderNo(orderNo);
        ReqOrderSaveVo result = order.toReqOrderSaveVo();
        return result;
    }

    public OrderDo getByOrderNo(String orderNo) {
        QueryWrapper<OrderDo> qw = new QueryWrapper<>();
        qw.eq("orderNo", orderNo);
        OrderDo result = this.getOne(qw);
        return result;
    }

    @Override
    public List<OrderDo> listByDeliveryStatus(String deliveryStatus) {
        QueryWrapper<OrderDo> qw = new QueryWrapper<>();
        qw.eq("deliveryStatus", deliveryStatus);
        return list(qw);
    }

    public List<ReqOrderSaveVo> toReqOrderSaveVo(List<OrderDo> orderList) {
        List<ReqOrderSaveVo> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orderList)) {
            for (OrderDo order : orderList) {
                result.add(order.toReqOrderSaveVo());
            }
            log.info("转换成ReqOrderSaveVo对象成功");
        }
        return result;
    }

    @Override
    public List<ReqOrderSaveVo> listReqOrderSaveByDeliveryStatus(String deliveryStatus) {
        try {
            List<ReqOrderSaveVo> result = toReqOrderSaveVo(listByDeliveryStatus(deliveryStatus));
            List<String> orderNos = result.stream().map(ReqOrderSaveVo::getOrderNo).collect(Collectors.toList());
            List<OrderItemsDo> orderItemsList = orderItemsDataService.listInOrderNo(orderNos);
            for (ReqOrderSaveVo item : result) {
                item.setOrderItem(toOrderItem(orderItemsList.stream().filter(orderItem -> orderItem.getOrderNo().equals(item.getOrderNo())).collect(Collectors.toList())));
            }
            return result;
        }catch (Exception e){
            String errMsg = String.format("listReqOrderSaveByDeliveryStatus error:{%s}",e.getMessage());
            log.error(errMsg);
            return null;
        }
    }

    @Override
    public void updateStatus(String orderNo, String status) {
        QueryWrapper<OrderDo> qw = new QueryWrapper<>();
        qw.eq("orderNo", orderNo);
        OrderDo orderDo = new OrderDo();
        orderDo.setDeliveryStatus(status);
        update(orderDo, qw);
    }
}


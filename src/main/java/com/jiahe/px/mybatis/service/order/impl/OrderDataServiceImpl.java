package com.jiahe.px.mybatis.service.order.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.model.order.OrderDo;
import com.jiahe.px.mybatis.dao.order.IOrderMapper;
import com.jiahe.px.mybatis.service.order.IOrderDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 批销订单数据服务
 * Date: 2024/7/17
 */
@Slf4j
@Service
public class OrderDataServiceImpl extends ServiceImpl<IOrderMapper, OrderDo> implements IOrderDataService {
}

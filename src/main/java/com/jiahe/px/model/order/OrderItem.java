package com.jiahe.px.model.order;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 批销订单明细
 * Date: 2024/7/17
 */
@Data
public class OrderItem implements Serializable {
    //商品编码
    @JSONField(ordinal = 1)
    private String goodsCode;
    //商品条码
    @JSONField(ordinal = 2)
    private String barCode;
    //数量
    @JSONField(ordinal = 3)
    private String num;
    //单位
    @JSONField(ordinal = 4)
    private String unit;
}

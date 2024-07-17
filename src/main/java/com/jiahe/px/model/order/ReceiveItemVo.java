package com.jiahe.px.model.order;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * 验收单明细
 * Date: 2024/7/17
 */
@Data
public class ReceiveItemVo implements Serializable {
    //商品编码
    @JSONField(ordinal = 1)
    private String goodsCode;
    //商品条码
    @JSONField(ordinal = 2)
    private String barCode;
    //验收数量
    @JSONField(ordinal = 3)
    private String receiveNum;
    //验收日期
    @JSONField(ordinal = 4)
    private String receiveDate;
}

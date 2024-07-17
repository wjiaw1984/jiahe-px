package com.jiahe.px.model.goods;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 批销商品价格明细
 * Date: 2024/7/16
 */
@Data
public class PxGoodsItem implements Serializable {
    private String customNo;
    private String price;
    private String inventory;
    private String goodsCode;
    private String barCode;
    private String matnr;
    private String isDirect;
}

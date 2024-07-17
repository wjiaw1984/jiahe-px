package com.jiahe.px.model.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 批销商品信息
 * Date: 2024/7/17
 */
@Data
@TableName(value = "px_goods_price")
public class PxGoodsPriceDo implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Date createTime;
    private Date lastUpdateTime;
    private int goodsId;
    private String customNo;
    private BigDecimal price;
    private String inventory;
    private String goodsCode;
    private String barCode;
    private String matnr;
    private String isDirect;

    @Override
    public String toString() {
        return "px_goods_price{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", goodsId=" + goodsId +
                ", customNo='" + customNo + '\'' +
                ", price='" + price + '\'' +
                ", inventory='" + inventory + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", matnr='" + matnr + '\'' +
                ", isDirect='" + isDirect + '\'' +
                '}';
    }
}

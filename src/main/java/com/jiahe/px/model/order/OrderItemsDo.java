package com.jiahe.px.model.order;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 批销订单明细信息
 * Date: 2024/7/17
 */
@Data
@TableName(value = "px_Order_Items")
public class OrderItemsDo implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String orderNo;
    private String goodsCode;
    private String barCode;
    private BigDecimal num;
    private String unit;
    private String deliveryNo;
    private String deliveryNum;
    private BigDecimal price;
    private String deliveryStatus;
    private Date deliveryDate;

    @Override
    public String toString() {
        return "px_Order_Items{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", goodsCode='" + goodsCode + '\'' +
                ", barCode='" + barCode + '\'' +
                ", num=" + num +
                ", unit='" + unit + '\'' +
                ", deliveryNo='" + deliveryNo + '\'' +
                ", deliveryNum='" + deliveryNum + '\'' +
                ", price=" + price +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", deliveryDate=" + deliveryDate +
                '}';
    }

    public OrderItem toOrderItem(){
        return JSONObject.from(this).toJavaObject(OrderItem.class);
    }
}

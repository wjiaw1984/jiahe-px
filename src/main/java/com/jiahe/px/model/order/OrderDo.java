package com.jiahe.px.model.order;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 批销订单表
 * Date: 2024/7/17
 */
@Data
@TableName(value = "px_order")
public class OrderDo implements Serializable {
    private Long id;
    private Date createTime;
    private Date lastUpdateTime;
    private String sheetId;
    private int sheettype;
    private String orderNo;
    private String shopNo;
    private String orderType;
    private String orderAddress;
    private String contactPerson;
    private String contactPhone;
    private Date orderDate;
    private Date deliveryDate;
    private String orderTitle;
    private String deliveryStatus;

    @Override
    public String toString() {
        return "px_order{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", lastUpdateTime=" + lastUpdateTime +
                ", sheetId='" + sheetId + '\'' +
                ", sheettype=" + sheettype +
                ", orderNo='" + orderNo + '\'' +
                ", shopNo='" + shopNo + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderAddress='" + orderAddress + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", orderTitle='" + orderTitle + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                '}';
    }
}

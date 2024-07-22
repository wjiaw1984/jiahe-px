package com.jiahe.px.model.order;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @JSONField(format = "yyyy-MM-dd")
    private Date orderDate;
    @JSONField(format = "yyyy-MM-dd")
    private Date deliveryDate;
    private String orderTitle;
    //0未交货 1部分交货 2全部交货 -1待推送 -2验收待推送
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

    public Order toOrder(){
        return JSONObject.from(this).toJavaObject(Order.class);
    }

    public ReqOrderSaveVo toReqOrderSaveVo(){
        return JSONObject.from(this).toJavaObject(ReqOrderSaveVo.class);
    }

    public ReqReceiveVo toReqReceiveVo(){
        ReqReceiveVo result = JSONObject.from(this).toJavaObject(ReqReceiveVo.class);
        return result;
    }
}

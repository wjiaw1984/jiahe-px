package com.jiahe.px.model.order;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.jiahe.px.common.core.utils.Convert;
import com.jiahe.px.model.YHRequestBase;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 批销订单信息
 * Date: 2024/7/17
 */
@Data
public class Order extends YHRequestBase {
    //订单号
    private String orderNo;
    //门店编码
    private String shopNo;
    //订单类型，目前固定值Z001
    private String orderType ;
    //收货地址
    private String orderAddress;
    //收货联系人
    private String contactPerson;
    //收货联系电话
    private String contactPhone;
    //订单日期：格式 2023-08-01
    //@JSONField(format = "yyyy-MM-dd")
    private String orderDate;
    //到店日期：计划发货日期，如果为空则取订单日期
    //@JSONField(format = "yyyy-MM-dd")
    private String deliveryDate;
    //订单备注
    private String orderTitle;


    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        if (!StringUtils.isEmpty(this.shopNo)) {
            json.put("shopNo", shopNo);
        }

        if (!StringUtils.isEmpty(this.orderNo)){
            json.put("orderNo", orderNo);
        }

        if (!StringUtils.isEmpty(this.orderType)) {
            json.put("orderType", orderType);
        }

        if (!StringUtils.isEmpty(this.orderAddress)) {
            json.put("orderAddress", orderAddress);
        }

        if (!StringUtils.isEmpty(this.contactPerson)) {
            json.put("contactPerson", contactPerson);
        }

        if (!StringUtils.isEmpty(this.contactPhone)) {
            json.put("contactPhone", contactPhone);
        }

        if (!StringUtils.isEmpty(this.orderDate )) {
            json.put("orderDate",this.orderDate);
        }
        if (!StringUtils.isEmpty(deliveryDate )){
            //json.put("deliveryDate", Convert.dateFormat(deliveryDate,"yyyy-MM-dd"));
            json.put("deliveryDate",this.deliveryDate);
        }

        if (!StringUtils.isEmpty(this.orderTitle)) {
            json.put("orderTitle", orderTitle);
        }

        return json;
    }
}

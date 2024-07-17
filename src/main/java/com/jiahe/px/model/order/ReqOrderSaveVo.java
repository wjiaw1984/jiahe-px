package com.jiahe.px.model.order;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Date: 2024/7/17
 */
@Data
public class ReqOrderSaveVo extends Order{
    //订单明细
    private List<OrderItem> orderItem;

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();

        if (!CollectionUtils.isEmpty(orderItem)) {
            json.put("orderItem", JSONArray.toJSONString(orderItem));
        }
        return json;
    }
}

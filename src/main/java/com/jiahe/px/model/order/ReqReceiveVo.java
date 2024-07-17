package com.jiahe.px.model.order;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 验收
 * Date: 2024/7/17
 */
@Data
public class ReqReceiveVo extends Order{
    private String deliveryNo;
    private List<ReceiveItemVo> receiveItem;

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObject = super.toJSON();
        if (!StringUtils.isEmpty(this.deliveryNo)){
            jsonObject.put("deliveryNo",this.deliveryNo);
        }
        if (!CollectionUtils.isEmpty(this.receiveItem)){
            jsonObject.put("receiveItem", JSONArray.toJSONString(this.receiveItem));
        }
        return jsonObject;
    }
}

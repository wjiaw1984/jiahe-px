package com.jiahe.px.model.goods;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jiahe.px.model.YHRequestBase;
import lombok.Data;

import java.security.Principal;
import java.util.List;

/**
 * 查询批销商品价格
 * Date: 2024/7/16
 */

@Data
public class ReqPxGoodsPriceVo extends YHRequestBase {
    private Integer curPageNo = 1;
    private List<PxGoodsItem> goods;

    @Override
    public JSONObject toJSON() {
        JSONObject json = super.toJSON();
        json.put("curPageNo", curPageNo);
        if (goods != null) {
            json.put("goods", JSONArray.toJSONString(goods));
        }
        return json;
    }
}

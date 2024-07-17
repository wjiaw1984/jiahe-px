package com.jiahe.px.service.goodsprice;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;

/**
 * 批销商品价格
 * Date: 2024/7/16
 */
public interface IPxGoodsPriceService {
    BaseResponse<ResPxGoodsPriceVo> goodsPriceQuery(ReqPxGoodsPriceVo entity);

    void syncGoodsPrice();
}

package com.jiahe.px.send;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;
public interface IHttpBaseCallService {
    BaseResponse<ResPxGoodsPriceVo> goodsPriceQuery(ReqPxGoodsPriceVo entity);
}

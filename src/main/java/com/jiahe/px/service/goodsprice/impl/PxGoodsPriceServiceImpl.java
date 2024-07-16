package com.jiahe.px.service.goodsprice.impl;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;
import com.jiahe.px.send.IHttpBaseCallService;
import com.jiahe.px.service.goodsprice.IPxGoodsPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 2024/7/16
 */
@Slf4j
@Service("com.jiahe.px.pxgoodsprice")
public class PxGoodsPriceServiceImpl implements IPxGoodsPriceService {
    @Autowired
    IHttpBaseCallService httpBaseCallService;

    @Override
    public BaseResponse<ResPxGoodsPriceVo> goodsPriceQuery(ReqPxGoodsPriceVo entity) {
        return httpBaseCallService.goodsPriceQuery(entity);
    }
}

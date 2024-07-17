package com.jiahe.px.service.goodsprice.impl;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.PxGoodsItem;
import com.jiahe.px.model.goods.ReqPxGoodsPriceVo;
import com.jiahe.px.model.goods.ResPxGoodsPriceVo;
import com.jiahe.px.mybatis.service.goods.IPxGoodsPriceDataService;
import com.jiahe.px.send.IHttpBaseCallService;
import com.jiahe.px.service.goodsprice.IPxGoodsPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Date: 2024/7/16
 */
@Slf4j
@Service("com.jiahe.px.pxgoodsprice")
public class PxGoodsPriceServiceImpl implements IPxGoodsPriceService {
    @Autowired
    IHttpBaseCallService httpBaseCallService;

    @Autowired
    IPxGoodsPriceDataService pxGoodsPriceDataService;

    @Override
    public BaseResponse<ResPxGoodsPriceVo> goodsPriceQuery(ReqPxGoodsPriceVo entity) {
        BaseResponse<ResPxGoodsPriceVo> result = httpBaseCallService.goodsPriceQuery(entity);
        if ("0".equals(result.getCode())){
            log.info("调用商品价格查询接口成功");
            if (!CollectionUtils.isEmpty(result.getData().getGoods())){
                pxGoodsPriceDataService.batchSaveOrUpdate(result.getData().getGoods());
                log.info("商品价格查询接口返回数据入库成功");
            }
        }
        return result;
    }
}

package com.jiahe.px.service.goodsprice.impl;

import com.jiahe.px.common.web.support.BaseResponse;
import com.jiahe.px.model.goods.PxGoodsItem;
import com.jiahe.px.model.goods.PxGoodsPriceDo;
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
        return result;
    }

    public ResPxGoodsPriceVo listPxGoodsPrice(ReqPxGoodsPriceVo entity) {
        ResPxGoodsPriceVo result = httpBaseCallService.goodsPriceQuery(entity).getData();
        return result;
    }

    @Override
    public void syncGoodsPrice() {
        ResPxGoodsPriceVo resPxGoodsPriceList;
        Integer pageNo = 0;
        do{
            pageNo = pageNo + 1;
            ReqPxGoodsPriceVo params = new ReqPxGoodsPriceVo();
            params.setCurPageNo(pageNo);
            resPxGoodsPriceList = listPxGoodsPrice(params);
            if (!CollectionUtils.isEmpty(resPxGoodsPriceList.getGoods())){
                try {
                    pxGoodsPriceDataService.batchSaveOrUpdate(resPxGoodsPriceList.getGoods());
                    log.info("商品价格查询页["+params.getCurPageNo()+"]接口返回数据入库成功");
                }catch (Exception e){
                    log.error("商品价格查询页["+params.getCurPageNo()+"]接口返回数据入库失败");
                }finally {
                    //保存成功后，清除内存中的数据，防止内存溢出
                    if (!CollectionUtils.isEmpty(resPxGoodsPriceList.getGoods())) {
                        resPxGoodsPriceList.getGoods().clear();
                    }
                    resPxGoodsPriceList = null;
                }
            }
        }while  (resPxGoodsPriceList.getTotalPage()>pageNo);
    }
}

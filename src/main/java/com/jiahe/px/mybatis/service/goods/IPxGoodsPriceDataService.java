package com.jiahe.px.mybatis.service.goods;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiahe.px.model.goods.PxGoodsItem;
import com.jiahe.px.model.goods.PxGoodsPriceDo;

import java.util.List;

/**
 * 批销商品信息数据服务
 * Date: 2024/7/17
 */
public interface IPxGoodsPriceDataService extends IService<PxGoodsPriceDo> {

    /*
     * @Description //TODO 比较批销系统返回的商品信息，更新数据表
     * @Date 14:58 2024/7/17
     * @Param [entity]
     * @return void
     **/
    void updateGoodsPrice(PxGoodsItem entity);

    PxGoodsPriceDo getByBarCode(String BarCode);

    Integer batchSaveOrUpdate(List<PxGoodsItem> list);

}

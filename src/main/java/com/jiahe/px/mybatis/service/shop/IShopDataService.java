package com.jiahe.px.mybatis.service.shop;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jiahe.px.model.shop.ShopDo;

import java.util.List;

public interface IShopDataService extends IService<ShopDo> {
    Integer batchDelete(List<ShopDo> records);
    List<ShopDo> listByShopIdsAndShopType(List<String> shopIds, Integer shopType);
    List<ShopDo> listNotByShopIds(List<String> shopIds);
}

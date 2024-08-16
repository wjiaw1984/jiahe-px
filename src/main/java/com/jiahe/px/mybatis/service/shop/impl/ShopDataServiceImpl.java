package com.jiahe.px.mybatis.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.model.shop.ShopDo;
import com.jiahe.px.mybatis.dao.shop.ShopMapper;
import com.jiahe.px.mybatis.service.shop.IShopDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service
public class ShopDataServiceImpl extends ServiceImpl<ShopMapper, ShopDo> implements IShopDataService {
    @Override
    public Integer batchDelete(List<ShopDo> records) {
        Assert.notEmpty(records, "error: records must not be empty");
        Integer i = 0;
        for (ShopDo item: records){
            if (this.removeById(item)){
                i++;
            }
        }
        return i;
    }


    @Override
    public List<ShopDo> listByShopIdsAndShopType(List<String> shopIds, Integer shopType) {
        QueryWrapper<ShopDo> qw = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(shopIds)) {
            qw.in("id", shopIds);
        }

        if (shopType != null) {
            qw.eq("shoptype", shopType);
        }
        return list(qw);
    }

    @Override
    public List<ShopDo> listNotByShopIds(List<String> shopIds) {
        QueryWrapper<ShopDo> qw = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(shopIds)) {
            qw.notIn("id", shopIds);
        }
        return list(qw);
    }
}

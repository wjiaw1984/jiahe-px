package com.jiahe.px.mybatis.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.common.core.utils.Convert;
import com.jiahe.px.model.goods.PxGoodsItem;
import com.jiahe.px.model.goods.PxGoodsPriceDo;
import com.jiahe.px.mybatis.dao.goods.IPxGoodsPriceMapper;
import com.jiahe.px.mybatis.service.goods.IPxGoodsPriceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 批销订单数据服务
 * Date: 2024/7/17
 */
@Slf4j
@Service
public class PxGoodsPriceDataServiceImpl extends ServiceImpl<IPxGoodsPriceMapper, PxGoodsPriceDo> implements IPxGoodsPriceDataService {
    @Override
    public void updateGoodsPrice(PxGoodsItem entity) {
        PxGoodsPriceDo pxGoodsPriceDo =updateByPxGoodsItem(entity);
        if (pxGoodsPriceDo != null){
            if (pxGoodsPriceDo.getId() == null){
                save(pxGoodsPriceDo);
            }else {
                pxGoodsPriceDo.setLastUpdateTime(new Date());
                updateById(pxGoodsPriceDo);
            }
        }
    }

    public PxGoodsPriceDo updateByPxGoodsItem(PxGoodsItem entity) {
        PxGoodsPriceDo pxGoodsPriceDo = getByGoodsCode(entity.getGoodsCode());
        if (pxGoodsPriceDo == null) {
            pxGoodsPriceDo = new PxGoodsPriceDo();
            if (!StringUtils.isEmpty(entity.getBarCode())) {
                pxGoodsPriceDo.setBarCode(entity.getBarCode());
            }
            if (!StringUtils.isEmpty(entity.getGoodsCode())) {
                pxGoodsPriceDo.setGoodsCode(entity.getGoodsCode());
            }
            if (!StringUtils.isEmpty(entity.getInventory())) {
                pxGoodsPriceDo.setInventory(Convert.ToDecimal(entity.getInventory()));
            }
            if (!StringUtils.isEmpty(entity.getPrice())) {
                pxGoodsPriceDo.setPrice(Convert.ToDecimal(entity.getPrice()));
            }

            pxGoodsPriceDo.setIsDirect(entity.getIsDirect());
            pxGoodsPriceDo.setMatnr(entity.getMatnr());
        } else {
            if (!pxGoodsPriceDo.getBarCode().equals(entity.getBarCode())) {
                pxGoodsPriceDo.setBarCode(entity.getBarCode());
            }

            if (pxGoodsPriceDo.getInventory().compareTo(Convert.ToDecimal(entity.getBarCode())) != 0) {
                pxGoodsPriceDo.setInventory(Convert.ToDecimal(entity.getInventory()));
            }

            if (pxGoodsPriceDo.getPrice().compareTo(Convert.ToDecimal(entity.getPrice())) != 0) {
                pxGoodsPriceDo.setPrice(Convert.ToDecimal(entity.getPrice()));
            }

            if (!pxGoodsPriceDo.getIsDirect().equals(entity.getIsDirect())) {
                pxGoodsPriceDo.setIsDirect(entity.getIsDirect());
            }

            if (!pxGoodsPriceDo.getMatnr().equals(entity.getMatnr())) {
                pxGoodsPriceDo.setMatnr(entity.getMatnr());
            }
        }
        return pxGoodsPriceDo;
    }


    @Override
    public PxGoodsPriceDo getByGoodsCode(String goodsCode) {
        QueryWrapper<PxGoodsPriceDo> qw = new QueryWrapper<>();
        qw.eq("goodsCode", goodsCode);
        return getOne(qw, false);
    }

    @Override
    public Integer batchSaveOrUpdate(List<PxGoodsItem> list) {
        Integer result = 0;
        if (!CollectionUtils.isEmpty(list)){
            for (PxGoodsItem entity : list){
                updateGoodsPrice(entity);
                result ++;
            }
        }
        return result;
    }
}

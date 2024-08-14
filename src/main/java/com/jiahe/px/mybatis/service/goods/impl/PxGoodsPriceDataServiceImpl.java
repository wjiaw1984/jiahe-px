package com.jiahe.px.mybatis.service.goods.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.px.common.base.BaseException;
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
        PxGoodsPriceDo pxGoodsPriceDo = updateByPxGoodsItem(entity);
        if (pxGoodsPriceDo != null) {
            if (pxGoodsPriceDo.getId() == null) {
                save(pxGoodsPriceDo);
            } else {
                pxGoodsPriceDo.setLastUpdateTime(new Date());
                updateById(pxGoodsPriceDo);
            }
        }
    }

    public PxGoodsPriceDo updateByPxGoodsItem(PxGoodsItem entity) {
        PxGoodsPriceDo pxGoodsPriceDo = getByBarCode(entity.getBarCode());
        if (pxGoodsPriceDo == null) {
            try {
                pxGoodsPriceDo = new PxGoodsPriceDo();
                if (!StringUtils.isEmpty(entity.getBarCode())) {
                    pxGoodsPriceDo.setBarCode(entity.getBarCode());
                }
                if (!StringUtils.isEmpty(entity.getGoodsCode())) {
                    pxGoodsPriceDo.setGoodsCode(entity.getGoodsCode());
                }
                if (!StringUtils.isEmpty(entity.getInventory())) {
                    pxGoodsPriceDo.setInventory(entity.getInventory());
                }
                if (!StringUtils.isEmpty(entity.getPrice())) {
                    pxGoodsPriceDo.setPrice(Convert.ToDecimal(entity.getPrice()));
                }
                if (!StringUtils.isEmpty(entity.getMaktx())) {
                    pxGoodsPriceDo.setMaktx(entity.getMaktx());
                }

                pxGoodsPriceDo.setIsDirect(entity.getIsDirect());
                pxGoodsPriceDo.setMatnr(entity.getMatnr());
            } catch (Exception e) {
                String errMsg = String.format("updateByPxGoodsItem {%s} error:{%s}", "生成数据表[px_goods_price]", e.getMessage());
                log.error(errMsg);
                throw new BaseException(errMsg);
            }
        } else {
            try {
                Boolean isUpdate = false;
                if (!pxGoodsPriceDo.getGoodsCode().equals(entity.getGoodsCode())) {
                    pxGoodsPriceDo.setGoodsCode(entity.getGoodsCode());
                    isUpdate = true;
                }

                if ((StringUtils.isEmpty(pxGoodsPriceDo.getInventory()) && !StringUtils.isEmpty(entity.getInventory()))
                        || (!StringUtils.isEmpty(pxGoodsPriceDo.getInventory()) && StringUtils.isEmpty(entity.getInventory()))
                        || (!StringUtils.isEmpty(pxGoodsPriceDo.getInventory()) && !StringUtils.isEmpty(entity.getInventory()))) {
                    if (!pxGoodsPriceDo.getInventory().equals(entity.getInventory())) {
                        pxGoodsPriceDo.setInventory(entity.getInventory());
                        isUpdate = true;
                    }
                }

                if ((pxGoodsPriceDo.getPrice() == null && !StringUtils.isEmpty(entity.getPrice()))
                        || (pxGoodsPriceDo.getPrice() != null && !StringUtils.isEmpty(entity.getPrice()))
                        || (pxGoodsPriceDo.getPrice() != null && StringUtils.isEmpty(entity.getPrice()))) {
                    if (pxGoodsPriceDo.getPrice().compareTo(Convert.ToDecimal(entity.getPrice())) != 0) {
                        pxGoodsPriceDo.setPrice(Convert.ToDecimal(entity.getPrice()));
                        isUpdate = true;
                    }
                }

                if ((StringUtils.isEmpty(pxGoodsPriceDo.getIsDirect()) && !StringUtils.isEmpty(entity.getIsDirect()))
                        || (!StringUtils.isEmpty(pxGoodsPriceDo.getIsDirect()) && StringUtils.isEmpty(entity.getIsDirect()))
                        || (!StringUtils.isEmpty(pxGoodsPriceDo.getIsDirect()) && !StringUtils.isEmpty(entity.getIsDirect()))) {
                    if (!pxGoodsPriceDo.getIsDirect().equals(entity.getIsDirect())) {
                        pxGoodsPriceDo.setIsDirect(entity.getIsDirect());
                        isUpdate = true;
                    }
                }

                if ((StringUtils.isEmpty(pxGoodsPriceDo.getMatnr()) && !StringUtils.isEmpty(entity.getMatnr()))
                        || (!StringUtils.isEmpty(pxGoodsPriceDo.getMatnr()) && StringUtils.isEmpty(entity.getMatnr()))
                        || (!StringUtils.isEmpty(pxGoodsPriceDo.getMatnr()) && !StringUtils.isEmpty(entity.getMatnr()))) {
                    if (!pxGoodsPriceDo.getMatnr().equals(entity.getMatnr())) {
                        pxGoodsPriceDo.setMatnr(entity.getMatnr());
                        isUpdate = true;
                    }
                }

                if (!isUpdate){
                    pxGoodsPriceDo = null;
                }
            } catch (Exception e) {
                String errMsg = String.format("updateByPxGoodsItem {%s} error:{%s}", "更新数据表[px_goods_price]", e.getMessage());
                log.error(errMsg);
                throw new BaseException(errMsg);
            }
        }
        return pxGoodsPriceDo;
    }


    @Override
    public PxGoodsPriceDo getByBarCode(String barCode) {
        QueryWrapper<PxGoodsPriceDo> qw = new QueryWrapper<>();
        qw.eq("barCode", barCode);
        return getOne(qw, false);
    }

    @Override
    public Integer batchSaveOrUpdate(List<PxGoodsItem> list) {
        Integer result = 0;
        if (!CollectionUtils.isEmpty(list)) {
            for (PxGoodsItem entity : list) {
                updateGoodsPrice(entity);
                result++;
            }
        }
        return result;
    }
}

package com.jiahe.px.model.goods;

import com.jiahe.px.common.web.support.BaseResponse;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询批销商品价格返回
 * Date: 2024/7/16
 */
@Data
public class ResPxGoodsPriceVo implements Serializable {
    private Integer curPage;
    private Integer totalPage;
    private List<PxGoodsItem> goods;
}

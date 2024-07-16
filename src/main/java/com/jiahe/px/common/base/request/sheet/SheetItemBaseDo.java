package com.jiahe.px.common.base.request.sheet;

import lombok.Data;

import java.io.Serializable;

/**
 * @PackageName：com.efuture.myshop.common.base.sheet
 * @ClassName：SheetHeadBaseDo
 * @Description：
 * @auther：wjw
 * @date：2023/3/20 11:28
 */
@Data
public class SheetItemBaseDo implements Serializable {
    /**
     * @Author wjw
     * @Description //TODO 单据编码
     * @Date 11:30 2023/3/20
     **/
    private String sheetId;
}

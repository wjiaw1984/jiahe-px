package com.jiahe.px.common.base.request.sheet;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @PackageName：com.efuture.myshop.common.base.sheet
 * @ClassName：SheetHeadBaseDo
 * @Description：
 */
@Data
public class SheetHeadBaseDo implements Serializable {
    /**
     * @Author wjw
     * @Description //TODO 单据编码
     * @Date 11:30 2023/3/20
     **/
    @TableId(type = IdType.NONE)
    private String sheetId;
    /**
     * @Author wjw
     * @Description //TODO 门店号
     * @Date 11:30 2023/3/20
     **/
    private String shopId;
    /**
     * @Author wjw
     * @Description //TODO 制单人名称
     * @Date 11:30 2023/3/20
     **/
    private String editor;
    /**
     * @Author wjw
     * @Description //TODO 制单日期
     * @Date 11:30 2023/3/20
     **/
    private Date editDate;
    /**
     * @Author wjw
     * @Description //TODO 业务员
     * @Date 11:30 2023/3/20
     **/
    private String operator;
    /**
     * @Author wjw
     * @Description //TODO 审核人
     * @Date 11:30 2023/3/20
     **/
    private String checker;
    /**
     * @Author wjw
     * @Description //TODO 审核人
     * @Date 11:30 2023/3/20
     **/
    private Date checkDate;
    /**
     * @Author wjw
     * @Description //TODO '确认标志 0-制单 100-确认';
     * @Date 11:30 2023/3/20
     **/
    private Integer flag;
}

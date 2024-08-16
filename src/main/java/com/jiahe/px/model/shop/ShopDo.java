package com.jiahe.px.model.shop;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description //TODO  机构表
 * @Date 14:32 2024/8/16
 * @Param
 * @return
 **/
@Data
@TableName(value = "shop")
public class ShopDo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 店号 */
    @TableId(type = IdType.NONE)
    private String id;

    /** 默认配送中心 */
    private String rationshopid;

    /** 店名 */
    private String name;

    /** 分店IP地址(前置机的ip,通讯用) */
    private String shopip;

    /** 店类型 0=总部,1=一级分公司(区域中心),2=二级分公司(城市中心),11=大卖场,12=标超, */
    private Integer shoptype;

    /** 联系电话号码 */
    private String linktele;

    /** 用拨号方式的电话号码,只对拨号店 */
    private String connecttele;

    /** 联网方法 0=直联,1=拨号联,2=与上级店同服务器 */
    private Integer connecttype;

    /** 上级店代码,总的的HeadID为自己 */
    private String headid;

    /** 状态 0=禁用 1=启用 */
    private Integer enable;

    /** 地址 */
    private String address;

    /** 营业面积 */
    private Integer workarea;

    /** 员工编制数 */
    private Integer workertotal;

    /** 门店配送方式（0-使用平价配送[默认的方式];1-使用加价批发配送[对于这种门店不走配送单，而走批发单]) */
    private Integer rationtype;

    /** 配送加价率 */
    private BigDecimal rationaddrate;

    /** 纳税号 */
    private String taxno;

    /** 开户银行 */
    private String khbank;

    /** 帐号 */
    private String accno;
}
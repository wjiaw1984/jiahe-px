package com.jiahe.px.common.base.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @PackageName：com.efuture.myshop.common.base.request
 * @ClassName：RequestPageBaseVo
 * @Description：
 * @date：2020/3/10 11:48
 */
@Data
public class RequestPageBaseVO<T> extends Page<T> implements Serializable {
    private String channel_id;
    private Integer page_no;
    private Integer page_size;
}

package com.jiahe.px.common.base.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * Created on 2019/11/13
 * 基础请求类
 * @since 1.0
 */
@Data
public class RequestBaseVO implements Serializable {
    /**
     * 渠道id
     */
    @JSONField(name = "channel_id")
    //@NotBlank(message = "渠道编码不能为空!")
    private String channel_id;
}

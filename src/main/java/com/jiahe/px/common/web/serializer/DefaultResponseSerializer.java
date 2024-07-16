package com.jiahe.px.common.web.serializer;

import com.alibaba.fastjson2.JSON;
import com.jiahe.px.common.web.aop.support.ExposerContext;
import com.jiahe.px.common.web.aop.support.ExposerContextHolder;
import com.jiahe.px.common.web.support.BaseResponse;
import org.springframework.util.StringUtils;

public class DefaultResponseSerializer implements ResponseSerializer{
    @Override
    public String serialize(BaseResponse baseResponse) {
        ExposerContext exposerContext = ExposerContextHolder.get();
        if(exposerContext == null || !StringUtils.hasText(exposerContext.getDateFormat())){
            return JSON.toJSONString(baseResponse);
        }

        return JSON.toJSONString(baseResponse);
    }
}

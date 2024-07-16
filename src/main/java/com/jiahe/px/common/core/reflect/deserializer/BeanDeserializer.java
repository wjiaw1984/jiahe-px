package com.jiahe.px.common.core.reflect.deserializer;


import com.alibaba.fastjson2.JSONObject;

public interface BeanDeserializer {
    Object deserializer(JSONObject jsonObject);

    Class supportType();
}


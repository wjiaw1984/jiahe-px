package com.jiahe.px.common.core.reflect;


import com.alibaba.fastjson2.JSONObject;

public interface ComponentInvoker {
    Object invoke(String componentName, String componentMethod, Object... args);

    Object invoke(String method, JSONObject jsonObject);
}
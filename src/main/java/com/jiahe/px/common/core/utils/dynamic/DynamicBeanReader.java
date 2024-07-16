package com.jiahe.px.common.core.utils.dynamic;

public interface DynamicBeanReader {

    /**
     * 动态加载bean
     *
     * @param dynamicBean
     */
    public void loadBean(DynamicBean dynamicBean);

    public void init();
}


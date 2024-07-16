package com.jiahe.px.common.web.aop.support;

public class ExposerContextHolder {
    private static final ThreadLocal<ExposerContext> threadLocal = new InheritableThreadLocal<ExposerContext>();

    private ExposerContextHolder() {

    }

    public static ExposerContext get() {
        return threadLocal.get();
    }

    public static void put(ExposerContext value) {
        threadLocal.set(value);
    }

    public static void remove() {
        if(get() != null) {
            threadLocal.remove();
        }
    }
}
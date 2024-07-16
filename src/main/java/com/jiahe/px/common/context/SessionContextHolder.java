package com.jiahe.px.common.context;

public class SessionContextHolder {
    private static final ThreadLocal<SessionContext> sessionThreadLocal = new InheritableThreadLocal<SessionContext>();

    private SessionContextHolder() {

    }

    public static SessionContext get() {
        return sessionThreadLocal.get();
    }

    public static void put(SessionContext value) {
        sessionThreadLocal.set(value);
    }

    public static void remove() {
        sessionThreadLocal.remove();
    }
}
package com.jiahe.px.common.core.utils.dynamic;

public abstract class DynamicBean {
    protected String beanName;

    public DynamicBean(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    protected abstract String getBeanXml();

    public String getXml() {
        StringBuffer buf = new StringBuffer();
        buf
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append(
                        "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")
                .append(
                        "       xmlns:p=\"http://www.springframework.org/schema/p\" xmlns:aop=\"http://www.springframework.org/schema/aop\"")
                .append(
                        "       xmlns:context=\"http://www.springframework.org/schema/context\" xmlns:jee=\"http://www.springframework.org/schema/jee\"")
                .append(
                        "       xmlns:tx=\"http://www.springframework.org/schema/tx\"")
                .append("       xsi:schemaLocation=\"")
                .append(
                        "           http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd")
                .append(
                        "           http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd")
                .append(
                        "           http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd")
                .append(
                        "           http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-2.5.xsd")
                .append(
                        "           http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd\">")
                .append(getBeanXml()).append("</beans>");
        return buf.toString();
    }

}

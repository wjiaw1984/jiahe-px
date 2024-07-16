package com.jiahe.px.common.core.utils.dynamic;

/**
 * 动态配置数据源
 *
 * @author hzf
 */
public class DataSourceDynamicBean extends DynamicBean {

    private String driverClassName; //驱动类

    private String url; //数据库连接地址

    private String username;//用户名

    private String password;//密码

    private String validationQuery;

    public DataSourceDynamicBean(String beanName) {
        super(beanName);
    }

    /**
     * 动态拼接数据源xml配置
     */
    @Override
    protected String getBeanXml() {
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf
                .append("<bean id=\""
                        + beanName
                        + "\" class=\"com.alibaba.druid.pool.DruidDataSource\" init-method=\"init\" destroy-method=\"close\">")
                .append(" <property name=\"url\" value=\"" + url + "\" />")
                .append(" <property name=\"username\" value=\"" + username + "\" />")
                .append(" <property name=\"password\" value=\"" + password + "\" />")
                .append(" <property name=\"validationQuery\" value=\"" + validationQuery + "\" />")
                .append("</bean>");
        return xmlBuf.toString();
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
}

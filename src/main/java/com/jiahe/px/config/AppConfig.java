package com.jiahe.px.config;

import com.jiahe.px.common.core.reflect.ComponentInvoker;
import com.jiahe.px.common.core.reflect.impl.SpringComponentInvoker;
import com.jiahe.px.common.core.utils.SpringContextHolder;
import com.jiahe.px.common.session.SessionContextBuilder;
import com.jiahe.px.common.session.impl.DefaultSessionContextBuilder;
import com.jiahe.px.common.web.serializer.DefaultResponseSerializer;
import com.jiahe.px.common.web.serializer.ResponseSerializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 取配置文件配置
 * Date: 2024/7/16
 */
@Component
@Data
@Configuration
public class AppConfig {
    @Value("${px.url}")
    private String url;

    @Value("${px.appId}")
    private String appId;

    @Value("${px.appSecret}")
    private String appSecret;

    @Value("${px.customNo}")
    private String customNo;

    @ConditionalOnMissingBean
    @Bean
    public ComponentInvoker componentInvoker(){
        return new SpringComponentInvoker();
    }

    @Bean
    public SpringContextHolder springContextHolder(){
        return new SpringContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionContextBuilder sessionContextBuilder(){
        return new DefaultSessionContextBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseSerializer responseSerializer(){
        return new DefaultResponseSerializer();
    }
}

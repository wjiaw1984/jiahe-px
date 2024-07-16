package com.jiahe.px.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 取配置文件配置
 * Date: 2024/7/16
 */
@Component
@Data
public class AppConfig {
    @Value("${px.url}")
    private String url;

    @Value("${px.appId}")
    private String appId;

    @Value("${px.appSecret}")
    private String appSecret;

    @Value("${px.customNo}")
    private String customNo;

}

package com.jiahe.px;

import com.alibaba.fastjson2.JSON;
import com.jiahe.px.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Date: 2024/7/16
 */
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class   //排除数据源
        })
@Slf4j
public class Application {
    public static void main(String[] args) {
        log.info("starting...");
        org.springframework.boot.SpringApplication.run(Application.class, args);
        log.info("家和批销系统启动成功！");
    }
}

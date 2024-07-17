package com.jiahe.px;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Date: 2024/7/16
 */
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class   //排除数据源
        })
@EnableTransactionManagement
@Slf4j
@MapperScan("com.jiahe.px.mybatis.dao")
public class Application {
    public static void main(String[] args) {
        log.info("starting...");
        org.springframework.boot.SpringApplication.run(Application.class, args);
        log.info("家和批销系统启动成功！");
    }
}

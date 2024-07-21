package com.jiahe.px.config;

import com.jiahe.px.job.SendOrderJob;
import com.jiahe.px.job.SendReceiptJob;
import com.jiahe.px.job.SyncGoodsPriceJob;
import com.jiahe.px.job.SyncOrderJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import java.util.Map;

/**
 * 定时作业quartz配置
 * Date: 2024/7/21
 */
@Configuration
public class QuartzConfig  {
    @Bean
    public JobDetail sendOrderJobDetail(){
        return JobBuilder
                .newJob(SendOrderJob.class)
                .withIdentity("sendOrderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sendOrderJobTrigger(){
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(sendOrderJobDetail())
                .withIdentity("sendOrderJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();
        return trigger;
    }

    @Bean
    public JobDetail sendReceiptJobDetail(){
        return JobBuilder
                .newJob(SendReceiptJob.class)
                .withIdentity("SendReceiptJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger sendReceiptJobTrigger(){
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(sendReceiptJobDetail())
                .withIdentity("sendReceiptJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/7 * * * * ?"))
                .build();
        return trigger;
    }

    @Bean
    public JobDetail syncOrderJobDetail(){
        return JobBuilder
                .newJob(SyncOrderJob.class)
                .withIdentity("syncOrderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger syncOrderJobTrigger(){
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(syncOrderJobDetail())
                .withIdentity("syncOrderJobTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * * * ?"))
                .build();
        return trigger;
    }

    @Bean
    public JobDetail syncGoodsPriceJobDetail() {
        return JobBuilder
                .newJob(SyncGoodsPriceJob.class)
                .withIdentity("syncGoodsPriceJob")
                .storeDurably()
                .build();
    }


    @Bean
    public Trigger syncGoodsPriceJobTrigger() {
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .forJob(syncGoodsPriceJobDetail())
                .withIdentity("syncGoodsPriceJobTrigger")
                // 每天凌晨1点执行
                .withSchedule(CronScheduleBuilder.cronSchedule("*/15 * * * * ?"))
                .build();
        return trigger;
    }

}

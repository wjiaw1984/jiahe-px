package com.jiahe.px.job;

import com.jiahe.px.service.order.IPxOrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 批销订单同步
 * Date: 2024/7/21
 */
@Slf4j
public class SyncOrderJob extends QuartzJobBean {
    @Autowired
    IPxOrderService pxOrderService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("SyncOrderJob start");
        //pxOrderService.syncOrder();
        log.info("SyncOrderJob end");
    }
}

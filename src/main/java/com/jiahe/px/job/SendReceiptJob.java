package com.jiahe.px.job;

import com.jiahe.px.service.order.IPxOrderService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 推送验收单
 * Date: 2024/7/21
 */
@Slf4j
public class SendReceiptJob extends QuartzJobBean {
    @Autowired
    IPxOrderService pxOrderService;

    @Override
    protected void executeInternal(org.quartz.JobExecutionContext context) throws JobExecutionException {
        log.info("推送验收单定时任务开始");
        // 执行推送验收单的逻辑
        //pxOrderService.sendReceipt();
        log.info("推送验收单定时任务结束");
    }
}

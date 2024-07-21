package com.jiahe.px.job;

import com.jiahe.px.service.goodsprice.IPxGoodsPriceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 同步批销商品
 * Date: 2024/7/21
 */
@Slf4j
public class SyncGoodsPriceJob extends QuartzJobBean {
    @Autowired
    IPxGoodsPriceService pxGoodsPriceService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("SyncGoodsPriceJob start");
        pxGoodsPriceService.syncGoodsPrice();
        log.info("SyncGoodsPriceJob end");
    }
}

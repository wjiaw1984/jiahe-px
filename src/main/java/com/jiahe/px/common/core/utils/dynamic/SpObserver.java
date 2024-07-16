package com.jiahe.px.common.core.utils.dynamic;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Yongdong
 *
 */
@Slf4j
public class SpObserver {
    private static ThreadLocal<String> local = new ThreadLocal<String>();
    public static final String DEFAULT_DS = "datasource1";


    public static void putSp(String sp) {
        log.info("切换到数据源{}",sp);
        local.set(sp);
        //MultiDataSource.applicationContext.refresh();
    }

    public static String getSp() {
        return local.get();
    }

    public static void clearSp() {
        local.remove();
    }


}

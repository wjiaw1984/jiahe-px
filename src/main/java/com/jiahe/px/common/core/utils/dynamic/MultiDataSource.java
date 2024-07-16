package com.jiahe.px.common.core.utils.dynamic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author wujiawen
 *
 */
@Slf4j
@Data
public class MultiDataSource extends AbstractRoutingDataSource {
    private static final String UNDERLINE = "_";
    private static MultiDataSource instance;
    private static byte[] lock=new byte[0];

    /**
     * 所有数据库
     */
    private Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
    private Map<Object, String> dataSourceInfoMap = new LinkedHashMap<>();


    @Override
    protected Object determineCurrentLookupKey() {
        return SpObserver.getSp();
    }

    /**
     * 添加数据源
     *
     * @param ds         数据源名称
     * @param dataSource 数据源
     */
    public synchronized void addDataSource(String ds, DataSource dataSource) {
        dataSourceMap.put(ds, dataSource);
        setTargetDataSources(dataSourceMap);
        afterPropertiesSet();
        log.info("动态数据源-加载 {} 成功", ds);
    }

    /**
     * 获取当前所有的数据源
     *
     * @return 当前所有数据源
     */
    public Map<Object, Object> getCurrentDataSources() {
        return dataSourceMap;
    }

    // 单例模式，保证获取到都是同一个对象，
    public static synchronized MultiDataSource getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance=new MultiDataSource();
                }
            }
        }
        return instance;
    }

}

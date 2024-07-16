package com.jiahe.px.common.core.utils;

import com.alibaba.fastjson.util.TypeUtils;
import com.efuture.myshop.core.bean.EasyBeanWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wzm on 2019/2/15.
 */
public class BeanMapUtils {
    /**
     * map转为bean. map中的key和bean中的字段 大小写可以不一致
     * @param map
     * @param entityClass
     * @return
     */
    public static Object mapToBean(Map map, Class entityClass){
        try {
            if(map == null){
                return null;
            }

            Map tmpMap = beautifyMap(map, entityClass);

            return TypeUtils.cast(tmpMap, entityClass, null);
        }catch (Exception e){
            ExceptionUtils.raise(e.getMessage());
        }

        return null;
    }

    /**
     * 按 bean 的字段名称格式化 map 中的key
     * @param map
     * @param entityClass
     * @return
     */
    public static Map beautifyMap(Map map, Class entityClass){
        Map tmpMap = new HashMap<>(map);

        try {
            EasyBeanWrapper easyBeanWrapper = new EasyBeanWrapper(entityClass.newInstance());

            Set<String> properties = easyBeanWrapper.getPropertiesSet();
            for (String property : properties) {
                if (map.containsKey(property)) {
                    continue;
                }

                Set<String> keys = map.keySet();
                for (String key : keys) {
                    if (property.equalsIgnoreCase(key)) {
                        tmpMap.put(property, map.get(key));
                        tmpMap.remove(key);
                    }
                }
            }
        }catch (Exception e){
            ExceptionUtils.raise(e.getMessage());
        }
        return tmpMap;
    }
}

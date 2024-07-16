package com.jiahe.px.common.core.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.util.*;

@Slf4j
public class SimpleBeanUtils {
    public static <T> T convertBean(Object orig, Class destClazz) {
        if(orig == null){
            return null;
        }

        Object dest = null;
        try {
            dest = destClazz.newInstance();

            copyProperties(dest, orig, true);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return (T) dest;
    }

    public static <T> List<T> convertBeanList(List origList, Class destClazz) {
        if(origList == null){
            return null;
        }

        List<T> destList = new ArrayList<T>();
        for(Object orig:origList){
            T obj = convertBean(orig, destClazz);
            destList.add(obj);
        }
        return destList;
    }

    public static <T> T copyProperties(Object dest, Object orig) {
        try {
            if(orig == null || dest == null){
                return null;
            }

            copyProperties(dest, orig, true);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return (T) dest;
    }

    public static <T> T copyProperties(Object dest, Object orig, String... ignoreProperties) {
        try {
            if(orig == null || dest == null){
                return null;
            }

            org.springframework.beans.BeanUtils.copyProperties(orig, dest, ignoreProperties);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
        return (T) dest;
    }

    /**
     *
     * @param dest
     * @param orgi
     * @param ignoreNull 值为空时，不拷贝该属性
     */
    public static boolean copyProperties(Object dest, Object orgi, boolean ignoreNull, String... ignoreProperties) {
        boolean changed = false;

        if(dest == null || orgi == null){
            return changed;
        }

        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        BeanWrapper orgiBW = new BeanWrapperImpl(orgi);
        BeanWrapper destBW = new BeanWrapperImpl(dest);

        for(PropertyDescriptor p:orgiBW.getPropertyDescriptors()){
            try {
                String propertryName = p.getName();

                if(!CollectionUtils.isEmpty(ignoreList) && ignoreList.contains(propertryName)){
                    continue;
                }

                if(propertryName.contains("class")){
                    continue;
                }

                Object orgiValue = orgiBW.getPropertyValue(propertryName);
                if(ignoreNull && orgiValue == null){
                    continue;
                }

                Object destValue = destBW.getPropertyValue(propertryName);

                //不等才做拷贝
                boolean eq = equals(orgiValue, destValue);
                if(!eq) {
                    destBW.setPropertyValue(p.getName(), orgiValue);

                    if (!changed) {
                        //值已经改变
                        changed = true;
                    }
                }
            }catch (Exception e){
                //log.error(e.getMessage());
            }
        }

        return changed;
    }

    /**
     * 拷贝属性，返回改变的属性
     * @param dest 目标对象
     * @param orgi 源对象
     * @param ignoreNull 如果值是null就不拷贝
     * @param ignoreProperties 不需要拷贝的属性
     * @return
     */
    public static Set<String> smartCopyProperties(Object dest, Object orgi, boolean ignoreNull, String... ignoreProperties) {
        Set<String> changedPropertyNameSet = null;

        if(dest == null || orgi == null){
            return changedPropertyNameSet;
        }

        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        BeanWrapper orgiBW = new BeanWrapperImpl(orgi);
        BeanWrapper destBW = new BeanWrapperImpl(dest);

        for(PropertyDescriptor p:orgiBW.getPropertyDescriptors()){
            try {
                String propertryName = p.getName();

                if(!CollectionUtils.isEmpty(ignoreList) && ignoreList.contains(propertryName)){
                    continue;
                }

                if(propertryName.contains("class")){
                    continue;
                }

                Object orgiValue = orgiBW.getPropertyValue(propertryName);
                if(orgiValue == null){
                    continue;
                }

                Object destValue = destBW.getPropertyValue(propertryName);

                //不等才做拷贝
                boolean eq = equals(orgiValue, destValue);
                if(!eq) {
                    destBW.setPropertyValue(p.getName(), orgiValue);

                    if(changedPropertyNameSet == null){
                        changedPropertyNameSet = new HashSet<String>();
                    }

                    changedPropertyNameSet.add(p.getName());
                }
            }catch (Exception e){
                //log.error(e.getMessage());
            }
        }

        return changedPropertyNameSet;
    }

    public static boolean equals(Object object1, Object object2){
        return !(object1 != null ? !object1.equals(object2) : object2 != null);
    }
}
package com.jiahe.px.common.core.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class EasyBeanWrapper extends BeanWrapperImpl {
    public static Map<String, Set<String>> beanPropertiesCache = new HashMap<String, Set<String>>();


    /**
     * Create a new BeanWrapperImpl for the given object.
     * @param object object wrapped by this BeanWrapper
     */
    public EasyBeanWrapper(Object object) {
        super(object);
    }

    /**
     * 属性如果为空，则填入值
     * @param propertyName
     * @param value
     */
    public void setPropertyValueIfNull(String propertyName, Object value){
        try {
            Set<String> propertiesSet = getPropertiesSet();
            if(!propertiesSet.contains(propertyName)){
                return;
            }

            Object orgiValue = this.getPropertyValue(propertyName);
            if(orgiValue != null){
                return;
            }

            this.setPropertyValue(propertyName, value);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 属性如果为空，则填入值
     * @param propertyName
     * @param value
     */
    public void setPropertyValueIfExist(String propertyName, Object value){
        try {
            Set<String> propertiesSet = getPropertiesSet();
            if(!propertiesSet.contains(propertyName)){
                return;
            }

            this.setPropertyValue(propertyName, value);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public Object getPropertyValueIfExist(String propertyName){
        try {
            Set<String> propertiesSet = getPropertiesSet();
            if(!propertiesSet.contains(propertyName)){
                return null;
            }

            return this.getPropertyValue(propertyName);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取该bean的所有属性
     * @return
     */
    public Set<String> getPropertiesSet(){
        String beanPropertiesCacheKey = this.getRootClass().toString();

        if(!beanPropertiesCache.containsKey(beanPropertiesCacheKey)){
            beanPropertiesCache.put(beanPropertiesCacheKey, buildPropertiesSet());
        }

        return beanPropertiesCache.get(beanPropertiesCacheKey);
    }

    private Set<String> buildPropertiesSet(){
        Set<String> propertiesSet = new HashSet<String>();

        PropertyDescriptor[] propertyDescriptors = this.getPropertyDescriptors();

        for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
            propertiesSet.add(propertyDescriptor.getName());
        }

        //去掉不必要字段
        propertiesSet.remove("class");

        return propertiesSet;
    }
}
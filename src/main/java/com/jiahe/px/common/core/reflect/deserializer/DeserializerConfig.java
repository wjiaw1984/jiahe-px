package com.jiahe.px.common.core.reflect.deserializer;

import java.util.HashMap;
import java.util.Map;

public class DeserializerConfig {
    private static DeserializerConfig global = new DeserializerConfig();

    private Map<Class,BeanDeserializer> beanDeserializerMap = new HashMap<Class, BeanDeserializer>();

    public static DeserializerConfig getGlobalInstance() {
        return global;
    }

    public void put(Class beanClazz, BeanDeserializer beanDeserializer){
        beanDeserializerMap.put(beanClazz, beanDeserializer);
    }

    public BeanDeserializer get(Class beanClazz){
        return beanDeserializerMap.get(beanClazz);
    }
}
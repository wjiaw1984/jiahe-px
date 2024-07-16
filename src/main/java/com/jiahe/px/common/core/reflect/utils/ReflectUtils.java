package com.jiahe.px.common.core.reflect.utils;


import com.alibaba.fastjson2.JSON;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO 反射类工具
 */
public class ReflectUtils {

    public static final boolean isJavaBean(Class<?> type){
        if(null == type )
            throw new NullPointerException();

        // 根据 getDeserializer 返回值类型判断是否为 java bean 类型
        try {
            PropertyDescriptor pd = new PropertyDescriptor(type.getSimpleName(), type);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public static final Class getClass(Object obj){
        Class clazz = obj.getClass();
        String className = clazz.getName();

        //spring代理类
        if(className.contains("$$")){
            String realClazzName = className.substring(0, className.indexOf("$$"));

            try {
                clazz = Class.forName(realClazzName);
            }catch (ClassNotFoundException e){
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        return clazz;
    }

    public static List<Field> getAllFields(Class clazz){
        List<Field> fieldList = new ArrayList<Field>() ;

        Class tempClass = clazz;

        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tempClass != null) {
            Field[] tmpFieldList = tempClass.getDeclaredFields();

            for(Field field:tmpFieldList){
                String str = field.toString();

                //不取静态字段
                if(str.contains("final ") || str.contains("static ")){
                    continue;
                }

                fieldList.add(field);
            }

            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }

        return fieldList;
    }

    public static Set<String> getAllFieldName(Class clazz){
        List<Field> fieldList = getAllFields(clazz);

        Set<String> fieleNameSet = new HashSet<String>();
        fieldList.forEach(item->{
            fieleNameSet.add(item.getName());
        });
        return fieleNameSet;
    }
}

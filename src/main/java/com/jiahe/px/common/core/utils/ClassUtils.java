package com.jiahe.px.common.core.utils;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;

/**
 * @PackageName：com.efuture.myshop.core.utils
 * @ClassName：ClassUtils
 * @Description：
 * @auther：wjw
 * @date：2021/9/26 11:41
 */
public class ClassUtils {
    public static Field[] getDeclaredFields(Class clazz){
        return clazz.getDeclaredFields();
    }

    public static QueryWrapper copyQueryWrapper(Class clazz,  JSONObject jsonObject){
        if (jsonObject == null || jsonObject.isEmpty()){
            return null;
        }
        QueryWrapper qw = new QueryWrapper();
        Field[] fields = clazz.getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            if (jsonObject.containsKey(fields[j].getName())){
                qw.eq(fields[j].getName(),jsonObject.get(fields[j].getName()));
            }
        }
        return qw;
    }
}


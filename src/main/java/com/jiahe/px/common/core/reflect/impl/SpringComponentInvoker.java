package com.jiahe.px.common.core.reflect.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.util.TypeUtils;
import com.jiahe.px.common.core.reflect.ComponentInvoker;
import com.jiahe.px.common.core.reflect.deserializer.BeanDeserializer;
import com.jiahe.px.common.core.reflect.deserializer.DeserializerConfig;
import com.jiahe.px.common.core.reflect.meta.ComponentInfoStore;
import com.jiahe.px.common.core.reflect.meta.support.MethodInfo;
import com.jiahe.px.common.core.reflect.meta.support.ParamInfo;
import com.jiahe.px.common.core.reflect.utils.ReflectUtils;
import com.jiahe.px.common.core.utils.ParamCheckUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SpringComponentInvoker implements ComponentInvoker, ApplicationContextAware {
    protected ApplicationContext applicationContext;
    protected ComponentInfoStore componentInfoStore;

    public SpringComponentInvoker(){
        componentInfoStore = new ComponentInfoStore();
    }

    @Override
    public Object invoke(String componentName, String componentMethod, Object... args) {
        Assert.hasText(componentName, "componentName is empty");
        Assert.hasText(componentMethod, "componentMethod is empty");

        Object component = this.applicationContext.getBean(componentName);

        Method method = findMethod(component.getClass(), componentMethod);
        if(method == null){
            throw new RuntimeException("未找到方法 "+componentMethod);
        }

        return ReflectionUtils.invokeMethod(method, component, args);
    }

    @Override
    public Object invoke(String methodStr, JSONObject jsonObject) {
        Assert.hasText(methodStr, "method is empty");

        if(!methodStr.contains(".")){
            throw new IllegalArgumentException("method name invalid");
        }

        int lastDot = methodStr.lastIndexOf(".");

        String componentName = methodStr.substring(0, lastDot);
        String componentMethod = methodStr.substring(lastDot + 1);

        Object component = this.applicationContext.getBean(componentName);
        Class componentClazz = ReflectUtils.getClass(component);

        Method method = findMethod(componentClazz, componentMethod);
        if(method == null){
            throw new RuntimeException("未找到方法 "+componentMethod);
        }

        MethodInfo methodInfo = componentInfoStore.get(componentClazz, componentMethod);
        //参数个数
        int paramCount = methodInfo.getParamCount();
        //带@RequestParam注解的参数个数
        int requestParamCount = methodInfo.getRequestParamCount();

        //没有参数
        if(methodInfo.getParamCount() == 0){
            return ReflectionUtils.invokeMethod(method, component);
        }

        if( requestParamCount == 0 && paramCount > 1 ){
            throw new RuntimeException(
                    MessageFormat.format("{0} 没有 @RequestParam 注解的情况下，只允许一个参数，且参数类型只能是 JsonObject/JavaBean", methodStr));
        }

        if( requestParamCount != 0 && requestParamCount != paramCount ){
            throw new RuntimeException(
                    MessageFormat.format("{0} 参数个数与 @RequestParam 个数不一致", methodStr));
        }

        //没有任何注解; 只允许一个参数，并且类型是 JSONObject / JavaBean
        if (requestParamCount == 0) {
            ParamInfo paramInfo = methodInfo.getParamInfoList().get(0);
            Class clazz = paramInfo.getType();

            if(jsonObject == null) {
                if ( (paramInfo.getRequestParam() != null && paramInfo.getRequestParam().required())
                        || paramInfo.getValidated() != null ){
                    throw new RuntimeException(
                            MessageFormat.format("{0}: {1}参数不能为空", methodStr, paramInfo.getParamName()));
                }
            }

            //参数是 jsonObject
            if(clazz.getName().equalsIgnoreCase(JSONObject.class.getName())){
                return ReflectionUtils.invokeMethod(method, component, jsonObject);
            }
            //参数是 javaBean
            else if(ReflectUtils.isJavaBean(clazz)){
                Object javaObject = toJavaObject(jsonObject, clazz);

                if(paramInfo.hasValidated()){
                    ParamCheckUtil.validate(javaObject);
                }
                return ReflectionUtils.invokeMethod(method, component, javaObject);
            }else {
                throw new RuntimeException(MessageFormat.format("{0}:{1} 只有一个参数并且没有@RequestParam注解的情况下，参数类型只能是 JsonObject/JavaBean ", methodStr, paramInfo.getParamName()));
            }
        }

        List<ParamInfo> paramInfoList = methodInfo.getParamInfoList();

        List<Object> objectList = new ArrayList<Object>();
        for(ParamInfo paramInfo:paramInfoList){
            RequestParam requestParam = paramInfo.getRequestParam();
            String param = paramInfo.getParamName();
            Class type = paramInfo.getType();

            Object value = jsonObject.get(param);
            Object targetValue = null;

            if(value == null){
                targetValue = null;
            }
            else if(value.getClass().getName().equalsIgnoreCase(type.getName())){
                targetValue = value;
            }
            else if(value instanceof JSONObject){
                targetValue = toJavaObject( (JSONObject)value, type);
            }
            else if(value instanceof JSONArray){
                targetValue = toJavaObjectList( (JSONArray)value, paramInfo.getActualType());
            }
            else{
                targetValue = TypeUtils.cast(value, type, null);
            }

            if(requestParam.required()){
                if(targetValue == null || (targetValue instanceof String && !StringUtils.hasText(targetValue.toString())) ) {
                    throw new RuntimeException(MessageFormat.format("参数{0}不能为空", param));
                }
            }

            if(paramInfo.hasValidated()){
                ParamCheckUtil.validate(targetValue);
            }

            objectList.add(targetValue);
        }

        return ReflectionUtils.invokeMethod(method, component, objectList.toArray());
    }

    private Object toJavaObject(JSONObject jsonObject, Class clazz){
        BeanDeserializer beanDeserializer = DeserializerConfig.getGlobalInstance().get(clazz);
        if(beanDeserializer != null){
            return beanDeserializer.deserializer(jsonObject);
        }

        return JSON.parseObject( jsonObject.toString(), clazz);
    }

    private Object toJavaObjectList(JSONArray jsonArray, Class clazz){
        return JSON.parseArray(jsonArray.toString(), clazz);
    }

    private Method findMethod(Class clazz, String methodName){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method:methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

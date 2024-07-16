package com.jiahe.px.common.core.reflect.meta;

import com.jiahe.px.common.core.reflect.meta.support.MethodInfo;
import com.jiahe.px.common.core.reflect.meta.support.ParamInfo;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ComponentInfoStore {

    private Map<String, MethodInfo> methodInfoMap;

    public ComponentInfoStore(){
        this.methodInfoMap = new HashMap<String, MethodInfo>();
    }

    public MethodInfo get(Class<?> clazz, String methodName){
        String methodKey = getMethodKey(clazz, methodName);

        if(this.methodInfoMap.containsKey(methodKey)){
            return this.methodInfoMap.get(methodKey);
        }

        Method method = findMethod(clazz, methodName);

        MethodInfo methodInfo = new MethodInfo(clazz, method);
        methodInfo.setParamCount(method.getParameterCount());

        //没有任何参数
        if(methodInfo.getParamCount() == 0){
            return methodInfo;
        }

        Class[] types = method.getParameterTypes();
        Type[] genericTypes = method.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = findAnnotation(method);

        for(int i=0; i<methodInfo.getParamCount(); i++){
            Class type = types[i];
            Type genericType = genericTypes[i];

            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setType(types[i]);
            paramInfo.setAnnotations(parameterAnnotations==null?null:parameterAnnotations[i]);

            try {
                if(genericType instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType)genericType;
                    Type[] typeList = parameterizedType.getActualTypeArguments();

                    paramInfo.setActualTypeList(typeList);

                    if(typeList != null && typeList.length>0) {
                        paramInfo.setActualType(Class.forName(typeList[0].getTypeName()));
                    }
                }
                //paramInfo.setActualType(Class.forName(type.getName()));
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(), e);
            }

            methodInfo.getParamInfoList().add(paramInfo);
        }

        this.methodInfoMap.put(methodKey, methodInfo);

        return methodInfo;
    }

    private String getMethodKey(Class clazz, String methodName){
        return MessageFormat.format("{0}.{1}", clazz.getName(), methodName);
    }

    private Method findMethod(Class clazz, String methodName){
        Method[] methods = clazz.getDeclaredMethods();
        Method result = null;

        int count = 0;
        for(Method method:methods) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                count++;
                if(count>1){
                    throw new RuntimeException(MessageFormat.format("{0} 找到多个方法 {1}", clazz.getName(), methodName));
                }
                result = method;
            }
        }

        return result;
    }

    private Annotation[][] findAnnotation(Method method){
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }

        if(parameterAnnotations.length == 1 && parameterAnnotations[0].length == 0){
            return null;
        }

        return parameterAnnotations;
    }

}
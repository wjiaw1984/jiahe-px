package com.jiahe.px.common.core.reflect.meta.support;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Data
public class MethodInfo {
    private Class clazz;
    private Method method;
    private String methodName;
    private Integer paramCount;
    private List<ParamInfo> paramInfoList;

    public MethodInfo(Class clazz, Method method){
        this.clazz = clazz;
        this.method = method;
        this.methodName = method.getName();

        paramCount = 0;
        paramInfoList = new ArrayList<ParamInfo>();
    }

    /**
     * 获取方法中有 @RequestParam 注解的参数个数
     * @return
     */
    public int getRequestParamCount(){
        int requestParamCount = 0;

        if(CollectionUtils.isEmpty(paramInfoList)){
            requestParamCount = 0;
        }else {
            for (ParamInfo paramInfo : paramInfoList) {
                if (paramInfo.hasRequestParam()) {
                    requestParamCount++;
                }
            }
        }

        return requestParamCount;
    }
}

package com.jiahe.px.common.core.reflect.meta.support;

import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Data
public class ParamInfo {
    private Class type;
    private Class actualType;
    private Type[] actualTypeList;
    private RequestParam requestParam;
    private Validated validated;
    private Valid valid;
    private Annotation[] annotations;
    private String paramName;

    public String getParamName(){
        if(paramName != null){
            return paramName;
        }

        if(requestParam == null){
            return null;
        }

        if(StringUtils.hasText(requestParam.value())){
            paramName = requestParam.value();
        }else if(StringUtils.hasText(requestParam.name())){
            paramName = requestParam.name();
        }

        return paramName;
    }

    public boolean hasRequestParam(){
        return requestParam != null;
    }

    public boolean hasValidated(){
        return valid != null || validated != null;
    }

    public void setAnnotations(Annotation[] annotations){
        this.annotations = annotations;

        if(annotations == null || annotations.length == 0){
            return;
        }

        for(int i=0; i<annotations.length; i++){
            Annotation annotation = annotations[i];

            if(annotation.annotationType().equals(RequestParam.class)){
                requestParam = (RequestParam) annotation;
            }
            else if(annotation.annotationType().equals(Validated.class)){
                validated = (Validated) annotation;
            }
            else if(annotation.annotationType().equals(Valid.class)){
                valid = (Valid) annotation;
            }
        }
    }
}
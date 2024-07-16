package com.jiahe.px.common.web.aop;

import com.jiahe.px.common.web.annotation.ExposerFormat;
import com.jiahe.px.common.web.aop.support.ExposerContext;
import com.jiahe.px.common.web.aop.support.ExposerContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)
public class ExposerFormatAop {
    @Pointcut("@annotation(com.efuture.myshop.common.web.annotation.ExposerFormat)")
    public void exposerFormatHandle(){}

    @Before("exposerFormatHandle()")
    public void doBefore(JoinPoint joinPoint){
        ExposerFormat exposerFormat = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(ExposerFormat.class);

        ExposerContext context =new ExposerContext();
        context.setDateFormat(exposerFormat.dateFormat());

        ExposerContextHolder.put(context);
    }

    @After("exposerFormatHandle()")
    public void doAfter(){
        //ExposerContextHolder.remove();
    }
}

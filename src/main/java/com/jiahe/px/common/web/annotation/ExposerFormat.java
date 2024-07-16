package com.jiahe.px.common.web.annotation;

import java.lang.annotation.*;

/**
 * Created by wzm on 2019/2/19.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExposerFormat {
    String dateFormat() default "";
}


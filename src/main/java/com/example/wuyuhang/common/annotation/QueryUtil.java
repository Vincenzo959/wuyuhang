package com.example.wuyuhang.common.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 卓佳伟
 * @date 2024/3/14 09:05
 * description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface QueryUtil {
    String colName() default "";
    String queryMode() default "eq";
    boolean customProcess() default false;
    String customClass() default "";
}

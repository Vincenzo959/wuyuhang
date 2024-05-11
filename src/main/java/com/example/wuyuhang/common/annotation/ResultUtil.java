package com.example.wuyuhang.common.annotation;

import java.lang.annotation.*;

/**
 * @author 卓佳伟
 * @date 2024/3/20 17:45
 * description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultUtil {

    //  初始数据
    String listEntityPropName() default "";

    //  当前字段对应辅表对象的字段名称
    String resultListEntityPropName() default "";

    //  当前字段使用辅表对象的service名称（填写实现类）
    String resultListUsedServiceName() default "";

    //  当前字段使用辅表对象的mapper名称
    String resultListUsedMapperName() default "";

    //  当前字段查询辅表对象Class
    Class serviceType() default Object.class;

    //  辅表查询字段名称
    String resultListEntityColName() default "id";

    //  辅表对象构建Map使用的键名
    String resultListMapKeyName() default "id";

    //  是否为复杂构建
    boolean isComplexObject() default false;
}

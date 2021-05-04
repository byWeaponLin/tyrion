package com.weaponlin.inf.tyrion.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表中的列，设定列名和是否是主键
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 列名
     * @return
     */
    String column();

    /**
     * 是否主键
     * @return
     */
    @Deprecated
    boolean pk() default false;

    /**
     * 是否需要忽略
     * @return
     */
    boolean isSkip() default false;
}
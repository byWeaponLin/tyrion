package com.weaponlin.inf.tyrion.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解，设置数据库名、表名以及分库分表配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    /**
     * database name
     */
    String database();

    /**
     * table name
     */
    String table();

    /**
     * 若不分库，值为0；若分库，值为分库的个数。<br>
     * 默认为0。
     */
    int databaseCount() default 0;

    /**
     * 若不分表，值为0；若分表，值为分表的个数。<br>
     * 默认为0。
     */
    int tableCount() default 0;

    /**
     * 分片数据库的分隔符
     * 比如""  那么database = demo55
     * 比如"_" 那么database = demo_55
     * @return
     */
    String databaseSeparator() default "";

    /**
     * 分片表的分隔符
     * 如数据库分隔符
     * @return
     */
    String tableSeparator() default "";
}


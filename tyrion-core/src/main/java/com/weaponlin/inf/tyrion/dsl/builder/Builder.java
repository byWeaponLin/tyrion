package com.weaponlin.inf.tyrion.dsl.builder;

import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.Executor;

import java.io.Serializable;
import java.util.List;

/**
 * Builder
 */
public interface Builder<R, T> extends Serializable {

    /**
     * 1. generate sql string
     * 2. generate SQLParameter(include sql-string, parameters, rowMapper and other info)
     * @return
     */
    SQLParameter<T, T> build();

    SQLParameter<R, T> build(Class<R> resultType);

    List<Object> getParameters();

    SQLType getSQLType();

    Executor getSQLExecutor();

    T fetchOne();

    R fetchOne(Class<R> resultTypeClass);

    List<T> fetch();

    List<R> fetch(Class<R> resultTypeClass);

    /**
     * TODO execute immediately
     */
    int exec();
}

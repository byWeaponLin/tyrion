package com.weaponlin.inf.tyrion.executor;

import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.executor.result.DefaultResultMapHandler;
import com.weaponlin.inf.tyrion.executor.result.ResultMapHandler;

import java.util.List;

/**
 * TODO to be continued
 */
public class DefaultExecutor implements Executor {

    @Override
    public <R, T> R selectOne(SQLParameter<R, T> sqlParameter) {
        return null;
    }

    @Override
    public <R, T> List<R> selectList(SQLParameter<R, T> sqlParameter) {
        return null;
    }

    @Override
    public int insert(SQLParameter sqlParameter) {
        return 0;
    }

    @Override
    public <T> int insert(T t) {
        return 0;
    }

    @Override
    public <T> int insert(List<T> list) {
        return 0;
    }

    @Override
    public int update(SQLParameter sqlParameter) {
        return 0;
    }

    @Override
    public int delete(SQLParameter sqlParameter) {
        return 0;
    }

    @Override
    public <T> ResultMapHandler<T> getResultMapHandler(List<SelectBuilder.RowMap> rowMaps, Class<T> resultType) {
        return new DefaultResultMapHandler<>(rowMaps, resultType);
    }
}

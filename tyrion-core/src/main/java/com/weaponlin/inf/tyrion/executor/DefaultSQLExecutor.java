package com.weaponlin.inf.tyrion.executor;

import com.weaponlin.inf.tyrion.dsl.builder.DeleteBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.InsertBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.builder.UpdateBuilder;

import java.util.List;

public class DefaultSQLExecutor {

    private final SQLExecutor sqlExecutor;

    public DefaultSQLExecutor(SQLExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    public <R, T> SelectBuilder<R, T> select() {
        return new SelectBuilder<>(sqlExecutor);
    }

    public InsertBuilder insert() {
        return new InsertBuilder(sqlExecutor);
    }

    public <T> int insert(T t) {
        return sqlExecutor.insert(t);
    }

    public <T> int batchInsert(List<T> list) {
        return sqlExecutor.insert(list);
    }

    public UpdateBuilder update() {
        return new UpdateBuilder(sqlExecutor);
    }

    public DeleteBuilder delete() {
        return new DeleteBuilder(sqlExecutor);
    }

    public <T> T getById(Object id, Class<T> entityClass) {
        return sqlExecutor.getById(id, entityClass);
    }

    public <T> int deleteById(Object id, Class<T> entityClass) {
        return sqlExecutor.deleteById(id, entityClass);
    }
}

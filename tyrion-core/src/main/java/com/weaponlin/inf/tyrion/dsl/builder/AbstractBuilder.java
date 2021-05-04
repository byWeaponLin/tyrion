package com.weaponlin.inf.tyrion.dsl.builder;

import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.SQLExecutor;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractBuilder<R, T> implements Builder<R, T> {

    protected final SQLType sqlType;
    protected final SQLExecutor sqlExecutor;

    AbstractBuilder(SQLType sqlType, SQLExecutor sqlExecutor) {
        this.sqlType = sqlType;
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public T fetchOne() {
        if (isSelect()) {
            return Optional.ofNullable(sqlExecutor)
                    .map(executor -> executor.selectOne(build()))
                    .orElseThrow(() -> new TyrionRuntimException("sqlExecutor is null, please set it."));
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    @Override
    public R fetchOne(Class<R> resultTypeClass) {
        if (isSelect()) {
            return Optional.ofNullable(sqlExecutor)
                    .map(executor -> executor.selectOne(build(resultTypeClass)))
                    .orElseThrow(() -> new TyrionRuntimException("sqlExecutor is null, please set it."));
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    @Override
    public List<T> fetch() {
        if (isSelect()) {
            return Optional.ofNullable(sqlExecutor)
                    .map(executor -> executor.selectList(build()))
                    .orElseThrow(() -> new TyrionRuntimException("sqlExecutor is null, please set it."));
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    @Override
    public List<R> fetch(Class<R> resultTypeClass) {
        if (isSelect()) {
            return Optional.ofNullable(sqlExecutor)
                    .map(executor -> executor.selectList(build(resultTypeClass)))
                    .orElseThrow(() -> new TyrionRuntimException("sqlExecutor is null, please set it."));
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    /**
     * only support insert/update/delete operation
     * @return
     */
    @Override
    public int exec() {
        if (sqlExecutor == null) {
            throw new TyrionRuntimException("sqlExecutor is null, please set it.");
        }
        if (isSelect()) {
            throw new UnsupportedOperationException("only support insert/update/delete operation");
        } else if (sqlType == SQLType.UPDATE) {
            return sqlExecutor.update(build());
        } else if (sqlType == SQLType.INSERT) {
            return sqlExecutor.insert(build());
        } else if (sqlType ==  SQLType.DELETE) {
            return sqlExecutor.delete(build());
        } else {
            throw new TyrionRuntimException("Illegal sqlType " + sqlType.name());
        }
    }

    @Override
    public SQLType getSQLType() {
        return sqlType;
    }

    @Override
    public SQLExecutor getSQLExecutor() {
        return sqlExecutor;
    }

    protected boolean isSelect() {
        return sqlType == SQLType.SELECT;
    }
}

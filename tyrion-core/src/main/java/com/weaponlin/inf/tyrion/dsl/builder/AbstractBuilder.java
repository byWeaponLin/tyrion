package com.weaponlin.inf.tyrion.dsl.builder;

import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.Executor;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractBuilder<R, T> implements Builder<R, T> {

    protected final SQLType sqlType;
    protected final Executor executor;

    AbstractBuilder(SQLType sqlType, Executor executor) {
        this.sqlType = sqlType;
        this.executor = executor;
    }

    @Override
    public T fetchOne() {
        if (isSelect()) {
            if (executor != null) {
                return executor.selectOne(build());
            } else {
                throw new TyrionRuntimException("executor is null, please set it.");
            }
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    @Override
    public R fetchOne(Class<R> resultTypeClass) {
        if (isSelect()) {
            if (executor != null) {
                return executor.selectOne(build(resultTypeClass));
            } else {
                throw new TyrionRuntimException("executor is null, please set it.");
            }
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    @Override
    public List<T> fetch() {
        if (isSelect()) {
            if (executor != null) {
                return executor.selectList(build());
            } else{
                throw new TyrionRuntimException("executor is null, please set it.");
            }
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    @Override
    public List<R> fetch(Class<R> resultTypeClass) {
        if (isSelect()) {
            if (executor != null) {
                return executor.selectList(build(resultTypeClass));
            } else {
                throw new TyrionRuntimException("executor is null, please set it.");
            }
        }
        throw new UnsupportedOperationException("only support select operation");
    }

    /**
     * only support insert/update/delete operation
     *
     * @return
     */
    @Override
    public int exec() {
        if (executor == null) {
            throw new TyrionRuntimException("executor is null, please set it.");
        }
        if (isSelect()) {
            throw new UnsupportedOperationException("only support insert/update/delete operation");
        } else if (sqlType == SQLType.UPDATE) {
            return executor.update(build());
        } else if (sqlType == SQLType.INSERT) {
            return executor.insert(build());
        } else if (sqlType == SQLType.DELETE) {
            return executor.delete(build());
        } else {
            throw new TyrionRuntimException("Illegal sqlType " + sqlType.name());
        }
    }

    @Override
    public SQLType getSQLType() {
        return sqlType;
    }

    @Override
    public Executor getSQLExecutor() {
        return executor;
    }

    protected boolean isSelect() {
        return sqlType == SQLType.SELECT;
    }
}

package com.weaponlin.inf.tyrion.dsl.builder;

import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.Executor;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * DeleteBuilder
 */
public class DeleteBuilder<R, T> extends AbstractBuilder<R, T> {
    private static final long serialVersionUID = 3648144059870595764L;

    private static final SQLType SQL_TYPE = SQLType.DELETE;

    public DeleteBuilder() {
        super(SQL_TYPE, null);
    }

    public DeleteBuilder(Executor executor) {
        super(SQL_TYPE, executor);
    }

    public FromBuilder<R, T> from(TableOperand<T> table) {
        checkNotNull(table, "table can not be null");
        return new FromBuilder(table, this);
    }

    @Override
    public String toString() {
        return "DELETE";
    }

    @Override
    public SQLParameter build() {
        throw new UnsupportedOperationException("DeleteBuilder not support this operation");
    }

    @Override
    public SQLParameter build(Class<R> resultType) {
        throw new UnsupportedOperationException("DeleteBuilder not support this operation");
    }

    @Override
    public List<Object> getParameters() {
        return null;
    }

    @Override
    public SQLType getSQLType() {
        return SQLType.DELETE;
    }

    @Override
    public T fetchOne() {
        throw new UnsupportedOperationException("DeleteBuilder do not support this operation");
    }

    @Override
    public R fetchOne(Class<R> resultTypeClass) {
        throw new UnsupportedOperationException("DeleteBuilder do not support this operation");
    }

    @Override
    public List<T> fetch() {
        throw new UnsupportedOperationException("DeleteBuilder do not support this operation");
    }

    @Override
    public List<R> fetch(Class<R> resultTypeClass) {
        throw new UnsupportedOperationException("DeleteBuilder do not support this operation");
    }

    @Override
    public int exec() {
        throw new UnsupportedOperationException("DeleteBuilder do not support this operation");
    }
}

package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class FromBuilder<R, T> extends AbstractBuilder<R, T> {
    private static final long serialVersionUID = -7640575610110436283L;

    private TableOperand<T> operand;

    private Builder previousBuilder;

    FromBuilder(TableOperand operand, SelectBuilder selectBuilder) {
        super(selectBuilder.sqlType, selectBuilder.executor);
        this.operand = checkNotNull(operand, "Table name shouldn't be null");
        this.previousBuilder = checkNotNull(selectBuilder, "Select part shouldn't be null");
    }

    FromBuilder(TableOperand operand, DeleteBuilder deleteBuilder) {
        super(deleteBuilder.sqlType, deleteBuilder.executor);
        this.operand = checkNotNull(operand, "Table name shouldn't be null");
        this.previousBuilder = checkNotNull(deleteBuilder, "Delete part shouldn't be null");
    }

    public WhereBuilder<R, T> where() {
        return new WhereBuilder<>(this);
    }

    @Override
    public String toString() {
        return previousBuilder.toString() + " FROM " + operand;
    }

    @Override
    public SQLParameter<T, T> build() {
        // TODO set resultMapHandler and rowMaps
        if (previousBuilder instanceof SelectBuilder) {
            SelectBuilder selectBuilder = (SelectBuilder) previousBuilder;
            // TODO modify result type
            return new SQLParameter<T, T>(toString(), getParameters(), selectBuilder.getRowMaps(),
                    operand.getEntityClazz());
        } else {
            return new SQLParameter<>(toString(), getParameters());
        }
    }

    @Override
    public SQLParameter<R, T> build(Class<R> resultType) {
        if (previousBuilder instanceof SelectBuilder) {
            SelectBuilder selectBuilder = (SelectBuilder) previousBuilder;
            return new SQLParameter<>(toString(), getParameters(), selectBuilder.getRowMaps(), resultType);
        } else {
            return new SQLParameter<>(toString(), getParameters());
        }
    }

    @Override
    public List<Object> getParameters() {
        checkNotNull(previousBuilder, "PreviousBuilder can not be null");
        return Optional.ofNullable(previousBuilder.getParameters()).orElse(Lists.newArrayList());
    }
}

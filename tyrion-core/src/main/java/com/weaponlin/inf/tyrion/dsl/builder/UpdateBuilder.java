package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.expression.CompareExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.enums.CompareOperator;
import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.Executor;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class UpdateBuilder<R, T> extends AbstractBuilder<R, T> {
    private static final long serialVersionUID = 4651243308668163270L;
    private static final SQLType SQL_TYPE = SQLType.UPDATE;

    private TableOperand table;

    private List<CompareExpressionOperand> assignments;

    public UpdateBuilder() {
        super(SQL_TYPE, null);
        assignments = Lists.newArrayList();
    }

    public UpdateBuilder(Executor executor) {
        super(SQL_TYPE, executor);
        assignments = Lists.newArrayList();
    }

    public UpdateBuilder table(TableOperand table) {
        checkNotNull(table, "table can not be null");
        this.table = table;
        return this;
    }

    public UpdateBuilder set(CompareExpressionOperand assignment) {
        checkNotNull(assignment, "assignment can not be null");
        checkNotNull(assignment.getOperator() == CompareOperator.EQ, "assignment only support EQ-Operator(=)");
        assignments.add(assignment);
        return this;
    }

    public WhereBuilder where() {
        return new WhereBuilder(this);
    }

    @Override
    public String toString() {
        checkState(isNotEmpty(assignments), "no assignments, please check sql");
        checkNotNull(table, "table can not be null");
        return "UPDATE "
                + table.toString(false)
                + assignments.stream().map(e -> e.toString(false)).collect(joining(", ", " SET ", ""));
    }

    @Override
    public SQLParameter<T, T> build() {
        return new SQLParameter<>(toString(), getParameters());
    }

    /**
     * @param resultType it is useless for UpdateBuilder
     */
    @Override
    public SQLParameter<R, T> build(Class<R> resultType) {
        return new SQLParameter<>(toString(), getParameters());
    }

    @Override
    public List<Object> getParameters() {
        return assignments.stream()
                .map(ExpressionOperand::getParameters)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList());
    }
}

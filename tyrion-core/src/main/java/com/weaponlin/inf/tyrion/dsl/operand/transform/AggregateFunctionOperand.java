package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.OperandExpressionOperand;
import com.weaponlin.inf.tyrion.enums.Aggregate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 聚合函数
 * sum、max、min、avg、count
 */
public class AggregateFunctionOperand extends FunctionOperand {
    private static final long serialVersionUID = 161277410730688617L;

    private Aggregate aggregate;

    private TransformOperand operand;

    AggregateFunctionOperand(TransformOperand operand, Aggregate aggregate) {
        super(operand.getName());
        this.operand = operand;
        this.aggregate = aggregate;
        super.parameters = operand.getParameters();
    }

    public static AggregateFunctionOperand max(String column) {
        checkArgument(isNotBlank(column), "column can not be blank");
        return max(ColumnOperand.name(column));
    }

    public static AggregateFunctionOperand max(TransformOperand operand) {
        checkNotNull(operand, "operand can not be null");
        return new AggregateFunctionOperand(operand, Aggregate.MAX);
    }

    public static AggregateFunctionOperand min(String column) {
        checkArgument(isNotBlank(column), "column can not be blank");
        return min(ColumnOperand.name(column));
    }

    public static AggregateFunctionOperand min(TransformOperand operand) {
        checkNotNull(operand, "operand can not be null");
        return new AggregateFunctionOperand(operand, Aggregate.MIN);
    }

    public static AggregateFunctionOperand avg(String column) {
        checkArgument(isNotBlank(column), "column can not be blank");
        return avg(ColumnOperand.name(column));
    }

    public static AggregateFunctionOperand avg(TransformOperand operand) {
        checkNotNull(operand, "operand can not be null");
        return new AggregateFunctionOperand(operand, Aggregate.AVG);
    }

    public static AggregateFunctionOperand sum(String column) {
        checkArgument(isNotBlank(column), "column can not be blank");
        return sum(ColumnOperand.name(column));
    }

    public static AggregateFunctionOperand sum(TransformOperand operand) {
        checkNotNull(operand, "operand can not be null");
        return new AggregateFunctionOperand(operand, Aggregate.SUM);
    }

    public static AggregateFunctionOperand count(String column) {
        checkArgument(isNotBlank(column), "column can not be blank");
        return count(ColumnOperand.name(column));
    }

    public static AggregateFunctionOperand count(TransformOperand operand) {
        checkNotNull(operand, "operand can not be null");
        return new AggregateFunctionOperand(operand, Aggregate.COUNT);
    }

    @Override
    public AggregateFunctionOperand as(String alias) {
        checkArgument(isNotBlank(alias), "alias can not be empty.");
        checkArgument(!alias.contains(" "), "alias can not exists space");
        this.alias = alias;
        return this;
    }

    private String fullFunction() {
        return aggregate.getFunctionName() + "(" + operand.toString(false) + ")";
    }

    @Override
    public String defaultAlias() {
        return aggregate.getFunctionName() + "(" + operand.defaultAlias() + ")";
    }

    @Override
    public String toString(boolean hasAlias) {
        return fullFunction() + decoratedAlias(hasAlias);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    @Override
    public ExpressionOperand toExpression() {
        return new OperandExpressionOperand(this);
    }
}

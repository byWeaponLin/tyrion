package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.OperandExpressionOperand;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ColumnOperand extends TransformOperand {
    private static final long serialVersionUID = 7564148803426063816L;

    ColumnOperand(String name) {
        super(name);
    }

    public static ColumnOperand name(String column) {
        checkArgument(StringUtils.isNotBlank(column), "column name can not be empty");
        return new ColumnOperand(column);
    }

    public static ColumnOperand column(String column) {
        checkArgument(StringUtils.isNotBlank(column), "column name can not be empty");
        return new ColumnOperand(column);
    }

    @Override
    public ColumnOperand as(String alias) {
        checkArgument(isNotBlank(alias), "alias can not be empty.");
        checkArgument(!alias.contains(" "), "alias can not exists space");
        this.alias = alias;
        return this;
    }

    @Override
    public String defaultAlias() {
        return name;
    }

    @Override
    public String toString(boolean hasAlias) {
        return name + decoratedAlias(hasAlias);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    @Override
    public ExpressionOperand toExpression() {
        return new OperandExpressionOperand(this);
    }

    @Override
    public List<Object> getParameters() {
        return Lists.newArrayList();
    }
}

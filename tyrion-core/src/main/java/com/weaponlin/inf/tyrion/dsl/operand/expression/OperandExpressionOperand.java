package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.operand.transform.TransformOperand;
import lombok.NonNull;

import java.util.Optional;

public class OperandExpressionOperand extends ExpressionOperand {
    private static final long serialVersionUID = 4122770030630205650L;

    private TransformOperand operand;

    public OperandExpressionOperand(@NonNull TransformOperand operand) {
        super(operand.getName());
        this.operand = operand;
        this.alias = operand.getAlias();
        super.parameters = Optional.ofNullable(operand.getParameters()).orElse(Lists.newArrayList());
    }

    @Override
    public String toString(boolean hasAlias) {
        return operand.toString(false) + decoratedAlias(hasAlias);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    @Override
    public String defaultAlias() {
        return operand.defaultAlias();
    }
}

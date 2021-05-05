package com.weaponlin.inf.tyrion.dsl.operand.transform;


import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;

import java.util.Arrays;
import java.util.List;

/**
 * VariableOperand
 */
public abstract class VariableOperand extends TransformOperand {
    private static final long serialVersionUID = -3585238394996936166L;

    VariableOperand(Object... values) {
        super("");
        super.parameters = Arrays.asList(values);
    }

    VariableOperand(List<?> values) {
        super("");
        super.parameters = (List<Object>) values;
    }

    @Override
    public ExpressionOperand toExpression() {
        throw new UnsupportedOperationException("can not convert VariableOperand to ExpressionOperand");
    }

    protected List<Object> getRealParameters() {
        return super.parameters;
    }

    @Override
    public TransformOperand as(String alias) {
        throw new UnsupportedOperationException("VariableOperand not support this operation");
    }

    @Override
    protected String decoratedAlias(boolean hasAlias) {
        throw new UnsupportedOperationException("VariableOperand not support this operation");
    }
}

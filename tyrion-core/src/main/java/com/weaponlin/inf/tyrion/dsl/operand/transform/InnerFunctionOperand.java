package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * TODO Inner Function will be done next time.
 */
public class InnerFunctionOperand extends FunctionOperand {
    private static final long serialVersionUID = 3141827431185669384L;

    private String alias;

    InnerFunctionOperand(String name) {
        super(name);
    }

    @Override
    public ExpressionOperand toExpression() {
        throw new UnsupportedOperationException("inner function not support now");
    }

    /**
     * @return
     */
    public static InnerFunctionOperand now() {
        return new InnerFunctionOperand("now()");
    }

    /**
     * TODO Consider that if the inner function has parameters?
     *
     * @return
     */
    public static InnerFunctionOperand rand() {
        return new InnerFunctionOperand("rand()");
    }

    @Override
    public InnerFunctionOperand as(String alias) {
        checkArgument(isNotBlank(alias), "alias can not be empty.");
        checkArgument(!alias.contains(" "), "alias can not exists space");
        this.alias = alias;
        return this;
    }

    @Override
    public String defaultAlias() {
        throw new UnsupportedOperationException("inner function not support now");
    }

    @Override
    public String toString(boolean hasAlias) {
        return name + decoratedAlias(hasAlias);
    }

    @Override
    public String toString() {
        return toString(true);
    }
}

package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class ExpressionOperand extends Operand {
    private static final long serialVersionUID = -6216349853355952178L;

    ExpressionOperand(String name) {
        super(name);
    }

    @Override
    public ExpressionOperand as(String alias) {
        checkArgument(isNotBlank(alias), "alias can not be empty");
        checkArgument(!alias.contains(" "), "alias can not exists space");
        this.alias = alias;
        return this;
    }

    @Override
    protected String decoratedAlias(boolean hasAlias) {
        return hasAlias && StringUtils.isNotBlank(alias) ? " AS " + alias : "";
    }
}

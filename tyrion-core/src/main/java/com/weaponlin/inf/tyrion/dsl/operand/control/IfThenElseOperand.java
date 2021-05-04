package com.weaponlin.inf.tyrion.dsl.operand.control;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.TransformOperand;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * interesting
 */
public class IfThenElseOperand extends ControlOperand {
    private static final long serialVersionUID = -8254982278446889442L;

    private boolean condition;

    private ExpressionOperand operand;

    IfThenElseOperand(boolean condition) {
        super("");
        this.condition = condition;
        this.operand = null;
    }

    public static IfThenElseOperand _if(boolean condition) {
        return new IfThenElseOperand(condition);
    }

    public IfThenElseOperand then(ExpressionOperand operand) {
        if (condition) {
            this.operand = operand;
        }
        return this;
    }

    public IfThenElseOperand then(TransformOperand operand) {
        if (condition) {
            this.operand = operand.toExpression();
        }
        return this;
    }

    public IfThenElseOperand _else(ExpressionOperand operand) {
        if (!condition) {
            this.operand = operand;
        }
        return this;
    }

    public IfThenElseOperand _else(TransformOperand operand) {
        if (!condition) {
            this.operand = operand.toExpression();
        }
        return this;
    }

    @Override
    public List<Object> getParameters() {
        return Optional.ofNullable(operand)
                .map(Operand::getParameters)
                .orElse(Lists.newArrayList());
    }

    @Override
    public String toString(boolean hasAlias) {
        if (operand == null) {
            return "";
        }
        return hasAlias && StringUtils.isNotBlank(alias)
                ? operand.toString(false) + " AS " + alias
                : operand.toString(false);
    }

    @Override
    public Operand as(String alias) {
        checkArgument(StringUtils.isNotBlank(alias), "Alias can not be empty");
        this.alias = alias;
        return this;
    }

    @Override
    protected String decoratedAlias(boolean hasAlias) {
        return toString(hasAlias);
    }

    @Override
    public String defaultAlias() {
        return operand.defaultAlias();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    @Override
    public List<ExpressionOperand> getResultExpression() {
        if (operand == null) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(this.operand);
    }
}

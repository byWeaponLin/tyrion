package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.enums.BooleanOperator;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

/**
 * TODO 子表达式
 */
public class NestedExpressionOperand extends ExpressionOperand {
    private static final long serialVersionUID = 6206390500895753029L;

    private List<ExpressionOperand> expressions;

    private BooleanOperator operator;

    private NestedExpressionOperand() {
        super("");
        expressions = Lists.newArrayList();
    }

    public NestedExpressionOperand and(ExpressionOperand expression) {
        check(BooleanOperator.AND);
        expressions.add(expression);
        return this;
    }

    public NestedExpressionOperand or(ExpressionOperand expression) {
        check(BooleanOperator.OR);
        expressions.add(expression);
        return this;
    }

    private void check(BooleanOperator operator) {
        if (this.operator == null) {
            this.operator = operator;
        } else if (this.operator != operator) {
            throw new IllegalStateException("AND and OR shouldn't be used in the same time");
        }
    }

    public static NestedExpressionOperand nested() {
        return new NestedExpressionOperand();
    }

    @Override
    public List<Object> getParameters() {
        return expressions.stream()
                .map(Operand::getParameters)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public String toString(boolean hasAlias) {
        if (CollectionUtils.isEmpty(expressions)) {
            return "";
        }
        return expressions.stream()
                .map(e -> e.toString(false))
                .collect(joining(operator.getOperator(), "(", ")"));
    }

    @Override
    public String defaultAlias() {
        return "";
    }

    @Override
    public String toString() {
        return toString(true);
    }
}

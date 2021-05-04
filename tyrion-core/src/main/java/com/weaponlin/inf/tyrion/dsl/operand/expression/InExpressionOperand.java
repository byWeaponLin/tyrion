package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.weaponlin.inf.tyrion.dsl.operand.transform.TransformOperand;
import com.weaponlin.inf.tyrion.enums.CompareOperator;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class InExpressionOperand extends ExpressionOperand {
    private static final long serialVersionUID = 3683824947894772826L;

    private TransformOperand left;
    private TransformOperand right;

    private CompareOperator operator;

    public InExpressionOperand(TransformOperand left, CompareOperator operator, TransformOperand right) {
        super(operator.getComparator());
        this.left = left;
        this.operator = operator;
        this.right = right;
        super.parameters = Stream.of(left.getParameters(), right.getParameters())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public String toString(boolean hasAlias) {
        return left.toString(false)
                + operator.getComparator()
                + "("
                + right.toString(false)
                + ")"
                + decoratedAlias(hasAlias);
    }

    @Override
    public String defaultAlias() {
        return left.defaultAlias()
                + operator.getComparator()
                + "("
                + right.defaultAlias()
                + ")";
    }

    @Override
    public String toString() {
        return toString(true);
    }
}

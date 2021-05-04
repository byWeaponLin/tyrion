package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.weaponlin.inf.tyrion.dsl.operand.transform.TransformOperand;
import com.weaponlin.inf.tyrion.enums.CompareOperator;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CompareExpressionOperand extends ExpressionOperand {
    private static final long serialVersionUID = 2554618149615907450L;

    private TransformOperand left;
    private TransformOperand right;

    @Getter
    private CompareOperator operator;


    public CompareExpressionOperand(TransformOperand left, CompareOperator operator, TransformOperand right) {
        super(operator.name());
        this.left = left;
        this.right = right;
        this.operator = operator;
        super.parameters = Stream.of(left.getParameters(), right.getParameters())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public String toString(boolean hasAlias) {
        return left.toString(false)
                + operator.getComparator()
                + right.toString(false)
                + decoratedAlias(hasAlias);
    }

    @Override
    public String defaultAlias() {
        return left.defaultAlias()
                + operator.getComparator()
                + right.defaultAlias();
    }

    @Override
    public String toString() {
        return toString(true);
    }
}

package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.weaponlin.inf.tyrion.dsl.operand.transform.TransformOperand;
import com.weaponlin.inf.tyrion.enums.CompareOperator;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BetweenExpressionOperand extends ExpressionOperand {
    private static final long serialVersionUID = -5917770564059569617L;

    private TransformOperand left;
    private TransformOperand value1;
    private TransformOperand value2;

    public BetweenExpressionOperand(TransformOperand left, TransformOperand value1, TransformOperand value2) {
        super(CompareOperator.BETWEEN_AND.name());
        this.left = left;
        this.value1 = value1;
        this.value2 = value2;
        super.parameters = Stream.of(left.getParameters(), value1.getParameters(), value2.getParameters())
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public String defaultAlias() {
        return left.defaultAlias()
                + " BETWEEN "
                + value1.defaultAlias()
                + " AND "
                + value2.defaultAlias();
    }

    @Override
    public String toString(boolean hasAlias) {
        return left.toString(false)
                + " BETWEEN "
                + value1.toString(false)
                + " AND "
                + value2.toString(false)
                + decoratedAlias(hasAlias);
    }

    @Override
    public String toString() {
        return toString(true);
    }
}

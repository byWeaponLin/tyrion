package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.control.ControlOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.VariableOperand;
import com.weaponlin.inf.tyrion.enums.BooleanOperator;
import com.weaponlin.inf.tyrion.enums.Order;
import com.weaponlin.inf.tyrion.enums.SQLType;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * TODO having count
 */
public class WhereBuilder<R, T> extends AbstractBuilder<R, T> {
    private static final long serialVersionUID = 1826524948263871298L;

    private Builder previousBuilder;
    private final List<ExpressionOperand> operands = Lists.newArrayList();
    private volatile BooleanOperator booleanIdentifier;

    private List<ColumnOperand> groupByColumns = Lists.newArrayList();

    private List<VariableOperand> limitOperands;

    private List<ColumnOperand> orderColumns = Lists.newArrayList();

    private ExpressionOperand havingOperand;

    private Order order;

    WhereBuilder(Builder previousBuilder) {
        super(previousBuilder.getSQLType(), previousBuilder.getSQLExecutor());
        this.previousBuilder = checkNotNull(previousBuilder, "previousBuilder can not be null");
    }

    public WhereBuilder<R, T> and(ExpressionOperand operand) {
        checkIdentificationStatus(BooleanOperator.AND);
        checkNotNull(operand, "Operand shouldn't be null");
        operands.add(operand);
        return this;
    }

    public WhereBuilder<R, T> or(ExpressionOperand operand) {
        checkIdentificationStatus(BooleanOperator.OR);
        checkNotNull(operand, "Operand shouldn't be null");
        operands.add(operand);
        return this;
    }

    public WhereBuilder<R, T> and(ControlOperand operand) {
        checkIdentificationStatus(BooleanOperator.AND);
        checkNotNull(operand, "Operand shouldn't be null");
        operands.addAll(operand.getResultExpression());
        return this;
    }

    public WhereBuilder<R, T> or(ControlOperand operand) {
        checkIdentificationStatus(BooleanOperator.OR);
        checkNotNull(operand, "Operand shouldn't be null");
        operands.addAll(operand.getResultExpression());
        return this;
    }

    public WhereBuilder<R, T> groupBy(String... columns) {
        checkNotNull(columns, "group by columns can not be null");
        groupByColumns.addAll(
                Arrays.stream(columns)
                        .filter(StringUtils::isNotBlank)
                        .map(ColumnOperand::name)
                        .collect(toList())
        );
        return this;
    }

    public WhereBuilder<R, T> groupBy(ColumnOperand... columns) {
        checkNotNull(columns, "group by columns can not be null");
        groupByColumns.addAll(Arrays.asList(columns));
        return this;
    }

    public WhereBuilder<R, T> limit(@NonNull Integer offset, @NonNull Integer limit) {
        this.limitOperands = Lists.newArrayList(PlaceholderOperand.value(offset), PlaceholderOperand.value(limit));
        return this;
    }

    public WhereBuilder<R, T> limit(@NonNull Integer limit) {
        this.limitOperands = Lists.newArrayList(PlaceholderOperand.value(limit));
        return this;
    }

    /**
     *
     */
    public WhereBuilder<R, T> having(ExpressionOperand operand) {
        checkNotNull(operand, "having condition can not be null");
        this.havingOperand = operand;
        return this;
    }

    public WhereBuilder<R, T> orderBy(String... columns) {
        checkNotNull(columns, "group by columns can not be null");
        this.orderColumns = Arrays.stream(columns)
                .map(ColumnOperand::column)
                .collect(toList());
        return this;
    }

    public WhereBuilder<R, T> orderBy(ColumnOperand... columns) {
        checkNotNull(columns, "group by columns can not be null");
        this.orderColumns = Arrays.asList(columns);
        return this;
    }

    public WhereBuilder<R, T> desc() {
        this.order = Order.DESC;
        return this;
    }

    public WhereBuilder<R, T> asc() {
        this.order = Order.ASC;
        return this;
    }

    private void checkIdentificationStatus(BooleanOperator identifier) {
        if (booleanIdentifier == null) {
            booleanIdentifier = identifier;
        } else if (booleanIdentifier != identifier) {
            throw new IllegalStateException("AND and OR shouldn't be used in the same time");
        }
    }

    private String conditionString() {
        if (CollectionUtils.isEmpty(operands)) {
            return "";
        }
        return operands.stream()
                .map(bit -> bit.toString(false))
                .filter(StringUtils::isNotBlank)
                .collect(joining(booleanIdentifier.getOperator(), " WHERE ", ""));
    }

    private String groupByString() {
        return Optional.ofNullable(groupByColumns)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list.stream()
                        .map(e -> e.toString(false))
                        .collect(joining(", ", " GROUP BY ", "")))
                .orElse("");
    }

    private String havingString() {
        return Optional.ofNullable(havingOperand)
                .map(e -> " HAVING " + e.toString(false))
                .orElse("");
    }

    private String orderByString() {
        String decoratedOrder = Optional.ofNullable(this.order)
                .map(Order::getOrder)
                .orElse("");
        return Optional.ofNullable(orderColumns)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list.stream()
                        .map(e -> e.toString(false))
                        .collect(joining(", ", " ORDER BY ", decoratedOrder)))
                .orElse("");
    }

    private String limitString() {
        return Optional.ofNullable(limitOperands)
                .filter(CollectionUtils::isNotEmpty)
                .map(list -> list.stream()
                        .map(e -> e.toString(false))
                        .collect(joining(", ", " LIMIT ", ""))
                ).orElse("");
    }

    @Override
    public String toString() {
        return previousBuilder.toString()
                + conditionString()
                + groupByString()
                + havingString()
                + orderByString()
                + limitString();
    }

    @Override
    public SQLParameter<T, T> build() {
        if (SQLType.SELECT == getSQLType()) {
            FromBuilder fromBuilder = (FromBuilder) previousBuilder;
            SelectBuilder selectBuilder = (SelectBuilder) fromBuilder.getPreviousBuilder();
            return new SQLParameter<T, T>(toString(), getParameters(), selectBuilder.getRowMaps(),
                    fromBuilder.getOperand().getEntityClazz());
        } else {
            return new SQLParameter<>(toString(), getParameters());
        }
    }

    @Override
    public SQLParameter<R, T> build(Class<R> resultType) {
        if (SQLType.SELECT == getSQLType()) {
            FromBuilder fromBuilder = (FromBuilder) previousBuilder;
            SelectBuilder selectBuilder = (SelectBuilder) fromBuilder.getPreviousBuilder();
            return new SQLParameter<>(toString(), getParameters(), selectBuilder.getRowMaps(), resultType);
        } else {
            return new SQLParameter<>(toString(), getParameters());
        }
    }

    @Override
    public List<Object> getParameters() {
        List<Object> conditionParameters = Optional.of(operands).map(ops -> ops.stream()
                .map(ExpressionOperand::getParameters)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList())
        ).orElse(Lists.newArrayList());
        List<Object> havingParameters = Optional.ofNullable(havingOperand).map(Operand::getParameters)
                .filter(CollectionUtils::isNotEmpty)
                .orElse(Lists.newArrayList());
        List<Object> limitParameters = Optional.ofNullable(limitOperands).map(ops -> ops.stream()
                .map(Operand::getParameters)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList())
        ).orElse(Lists.newArrayList());
        return Stream.of(previousBuilder.getParameters(), conditionParameters, havingParameters, limitParameters)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(toList());
    }
}

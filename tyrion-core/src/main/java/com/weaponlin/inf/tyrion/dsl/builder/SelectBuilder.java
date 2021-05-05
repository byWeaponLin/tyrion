package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.control.ControlOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.ExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.TransformOperand;
import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.Executor;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;

public class SelectBuilder<R, T> extends AbstractBuilder<R, T> {
    private static final long serialVersionUID = 5394178704845865940L;

    private static final SQLType SQL_TYPE = SQLType.SELECT;

    private List<ExpressionOperand> columns;

    /**
     * The columns we are going to select
     * IMPORTANT: RowMap 和 标注@Table的类的属性 取交集
     */
    @Getter
    private List<RowMap> rowMaps;

    public SelectBuilder() {
        super(SQL_TYPE, null);
        this.columns = Lists.newArrayList();
        this.rowMaps = Lists.newArrayList();
    }

    public SelectBuilder(Executor executor) {
        super(SQL_TYPE, executor);
        this.columns = Lists.newArrayList();
        this.rowMaps = Lists.newArrayList();
    }

    /**
     * select all columns
     * @return
     */
    public SelectBuilder<R, T> columns() {
        return this;
    }

    public SelectBuilder<R, T> column(String... columns) {
        checkNotNull(columns, "Operands shouldn't be null");
        List<ExpressionOperand> columnOperands = Arrays.stream(columns)
                .map(ColumnOperand::name)
                .map(ColumnOperand::toExpression)
                .collect(toList());
        this.columns.addAll(columnOperands);
        setRowMap(columns);
        return this;
    }

    public SelectBuilder<R, T> column(ExpressionOperand... operands) {
        checkNotNull(operands, "Operands shouldn't be null");
        columns.addAll(Arrays.asList(operands));
        setRowMap(operands);
        return this;
    }

    public SelectBuilder<R, T> column(TransformOperand... operands) {
        checkNotNull(operands, "Operands shouldn't be null");
        columns.addAll(Arrays.stream(operands).map(TransformOperand::toExpression).collect(toList()));
        setRowMap(operands);
        return this;
    }

    public SelectBuilder<R, T> column(ControlOperand... operands) {
        checkNotNull(operands, "Operands shouldn't be null");
        List<ExpressionOperand> controlOperands = Arrays.stream(operands)
                .map(ControlOperand::getResultExpression)
                .flatMap(Collection::stream)
                .collect(toList());

        columns.addAll(controlOperands);
        setRowMap(controlOperands);
        return this;
    }

    @Override
    public String toString() {
        if (CollectionUtils.isEmpty(columns)) {
            return "SELECT * ";
        }
        return columns.stream()
                .map(selectColumn -> selectColumn.toString(true))
                .filter(StringUtils::isNotBlank).collect(joining(", ", "SELECT ", ""));
    }

    public FromBuilder<R, T> from(TableOperand operand) {
        if (operand == null) {
            throw new TyrionRuntimException("table not found");
        }
        if (CollectionUtils.isEmpty(rowMaps)) {
            setRowMap(operand.getColumns());
        }
        return new FromBuilder<>(operand, this);
    }

    @Override
    public SQLParameter<T, T> build() {
        return new SQLParameter<>(toString(), getParameters(), rowMaps);
    }

    @Override
    public SQLParameter<R, T> build(Class<R> resultType) {
        return new SQLParameter<>(toString(), getParameters(), rowMaps, resultType);
    }

    @Override
    public List<Object> getParameters() {
        if (isEmpty(columns)) {
            return Lists.newArrayList();
        }
        return columns.stream()
                .map(ExpressionOperand::getParameters)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void setRowMap(String... columns) {
        rowMaps.addAll(Arrays.stream(columns)
                .map(e -> new RowMap().setColumn(e))
                .collect(toList()));
    }

    /**
     * set alias
     *
     * @param operands
     */
    private void setRowMap(Operand... operands) {
        // get current method's result type, and then analysis result type's properties mapping to fields
        setRowMap(Arrays.asList(operands));
    }

    private void setRowMap(List<? extends Operand> operands) {
        List<RowMap> maps = operands.stream()
                .filter(Objects::nonNull)
                .map(operand -> Optional.ofNullable(operand.getAlias())
                        .filter(StringUtils::isNotBlank)
                        .orElse(operand.defaultAlias()))
                .map(p -> new RowMap().setColumn(p))
                .collect(Collectors.toList());
        rowMaps.addAll(maps);
    }

    @Data
    @Accessors(chain = true)
    public static class RowMap {
        private String property;
        private Class javaType;

        private String column;
        private Class jdbcType;
    }
}

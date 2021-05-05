package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.VariableOperand;
import com.weaponlin.inf.tyrion.enums.SQLType;
import com.weaponlin.inf.tyrion.executor.Executor;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.stream.Collectors.joining;

public class InsertBuilder<R, T> extends AbstractBuilder<R, T> {
    private static final long serialVersionUID = -6682031211557475666L;

    private static final SQLType SQL_TYPE = SQLType.INSERT;

    private TableOperand table;

    private List<ColumnOperand> columns;

    private List<VariableOperand> values;

    public InsertBuilder() {
        super(SQL_TYPE, null);
        this.columns = Lists.newArrayList();
        this.values = Lists.newArrayList();
    }

    public InsertBuilder(Executor executor) {
        super(SQL_TYPE, executor);
        this.columns = Lists.newArrayList();
        this.values = Lists.newArrayList();
    }

    public InsertBuilder into(TableOperand table) {
        checkNotNull(table, "table can not be null");
        this.table = table;
        return this;
    }

    /**
     * all columns
     *
     * @return
     */
    public InsertBuilder columns() {
        checkNotNull(table, "table can not be null, please set table first");
        columns = table.getColumns();
        if (CollectionUtils.isEmpty(columns)) {
            throw new TyrionRuntimException("not insert any column, please check the entity class's " +
                    "field whether it has already annotated with @Column");
        }
        return this;
    }

    public InsertBuilder columns(String... columns) {
        checkNotNull(columns, "columns can not be null");
        this.columns.addAll(Arrays.stream(columns)
                .filter(StringUtils::isNotBlank)
                .map(ColumnOperand::name)
                .collect(Collectors.toList())
        );
        return this;
    }

    public InsertBuilder values(Object... values) {
        checkNotNull(values, "values can not be null");
        this.values.addAll(Arrays.stream(values)
                .map(PlaceholderOperand::value)
                .collect(Collectors.toList())
        );
        return this;
    }

    @Override
    public String toString() {
        checkState(columns.size() > 0, "columns's size must be greater than zero");
        checkState(values.size() > 0, "values's size must be greater than zero");
        checkState(values.size() % columns.size() == 0,
                "Illegal SQLParameter, columns's size is not equal to values'size, column size = %s, value size = %s",
                columns.size(),
                values.size());

        String valueHolder = Collections.nCopies(columns.size(), "?").stream().collect(joining(", ", "(", ")"));
        return "INSERT INTO "
                + table.toString(false)
                + columns.stream().map(e -> e.toString(false)).collect(joining(", ", "(", ")"))
                + " VALUES"
                + valueHolder;
    }

    @Override
    public SQLParameter<T, T> build() {
        return new SQLParameter<>(columns, getParameters(), toString());
    }

    /**
     * @param resultType it is useless for InsertBuilder
     */
    @Override
    public SQLParameter<R, T> build(Class<R> resultType) {
        return new SQLParameter<>(columns, getParameters(), toString());
    }

    @Override
    public List<Object> getParameters() {
        return values.stream()
                .map(Operand::getParameters)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}

package com.weaponlin.inf.tyrion.dsl;

import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.executor.result.DefaultResultMapHandler;
import com.weaponlin.inf.tyrion.executor.result.ResultMapHandler;
import lombok.Getter;

import java.util.List;


/**
 * SQLGenerator
 *
 * @param <R> result type class
 * @param <T> entity class type, must annotate with @Table
 */
@Getter
public class SQLParameter<R, T> {

    /**
     * generated sql, it is beautiful.
     */
    private String sql;

    /**
     * parameters for the placeholders
     */
    private List<Object> parameters;

    private List<ColumnOperand> insertColumns;

    /**
     * selected rows
     */
    private List<SelectBuilder.RowMap> rowMaps;

    private Class<R> resultType;
    private ResultMapHandler<R> resultMapHandler;

    /**
     * TODO
     */
    private Class<T> entityType;

    public SQLParameter(String sql, List<Object> parameters) {
        this.sql = sql;
        this.parameters = parameters;
    }

    public SQLParameter(List<ColumnOperand> insertColumns, List<Object> parameters, String sql) {
        this.sql = sql;
        this.parameters = parameters;
        this.insertColumns = insertColumns;
    }

    public SQLParameter(String sql, List<Object> parameters, List<SelectBuilder.RowMap> rowMaps) {
        this.sql = sql;
        this.parameters = parameters;
        this.rowMaps = rowMaps;
    }

    public SQLParameter(String sql, List<Object> parameters, List<SelectBuilder.RowMap> rowMaps, Class<R> resultType) {
        this.sql = sql;
        this.parameters = parameters;
        this.rowMaps = rowMaps;
        this.resultType = resultType;
        this.resultMapHandler = new DefaultResultMapHandler<>(rowMaps, resultType);
    }
}

package com.weaponlin.inf.tyrion.executor;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.annotation.Column;
import com.weaponlin.inf.tyrion.datasource.PooledDatasource;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import com.weaponlin.inf.tyrion.executor.result.DefaultResultMapHandler;
import com.weaponlin.inf.tyrion.executor.result.ResultMapHandler;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand.table;
import static java.util.stream.Collectors.toList;

/**
 * with pooled datasource
 */
public class DefaultExecutor implements Executor {

    private final PooledDatasource pooledDatasource;

    public DefaultExecutor(PooledDatasource pooledDatasource) {
        this.pooledDatasource = pooledDatasource;
    }

    @Override
    public <R, T> R selectOne(SQLParameter<R, T> sqlParameter) {
        List<R> list = selectList(sqlParameter);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <R, T> List<R> selectList(SQLParameter<R, T> sqlParameter) {
        try (Connection connection = pooledDatasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlParameter.getSql());
            setParameters(preparedStatement, sqlParameter);
            ResultSet resultSet = preparedStatement.executeQuery();
            return new DefaultResultMapHandler<>(sqlParameter.getRowMaps(), sqlParameter.getResultType())
                    .mapRows(resultSet);
        } catch (Exception e) {
            throw new TyrionRuntimException("query data failed", e);
        }
    }

    @Override
    public int insert(SQLParameter sqlParameter) {
        List<ColumnOperand> insertColumns = sqlParameter.getInsertColumns();
        if (CollectionUtils.isEmpty(insertColumns)) {
            throw new TyrionRuntimException("no insert columns");
        }
        List<Object> parameters = sqlParameter.getParameters();
        Integer parameterSize = Optional.ofNullable(parameters)
                .map(List::size)
                .orElse(0);
        int columnSize = insertColumns.size();
        if (parameterSize == columnSize) {
            return update0(sqlParameter);
        } else if (parameterSize % columnSize == 0 && parameterSize > 0) {
            List<Object[]> partitionObjects = Lists.partition(parameters, columnSize).stream()
                    .map(List::toArray)
                    .collect(toList());
            return batchUpdate(sqlParameter, partitionObjects);
        } else {
            throw new TyrionRuntimException("parameterSize % columnSize != 0");
        }

    }

    private int update0(SQLParameter sqlParameter) {
        try (Connection connection = pooledDatasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlParameter.getSql());
            setParameters(preparedStatement, sqlParameter);
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new TyrionRuntimException("update data failed", e);
        }
    }

    /**
     * TODO 配置rewriteBatchedStatements=true，效率更高
     * https://www.cnblogs.com/chenjianjx/archive/2012/08/14/2637914.html
     */
    private int batchUpdate(SQLParameter sqlParameter, List<Object[]> partitionObjects) {
        try (Connection connection = pooledDatasource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlParameter.getSql());
            for (Object[] parameters : partitionObjects) {
                for (int i = 0; i < parameters.length; i++) {
                    preparedStatement.setObject(i + 1, parameters[i]);
                }
                preparedStatement.addBatch();
            }
            int[] res = preparedStatement.executeBatch();
            return Arrays.stream(res).sum();
        } catch (Exception e) {
            throw new TyrionRuntimException("batch update data failed", e);
        }
    }

    @Override
    public <T> int insert(T t) {
        return insert(Lists.newArrayList(t));
    }

    @Override
    public <T> int insert(List<T> list) {
        checkArgument(CollectionUtils.isNotEmpty(list), "insert objects can not be empty");
        // check the object's type
        list.stream().filter(Objects::nonNull).reduce((a, b) -> {
            if (a.getClass() == b.getClass()) {
                return b;
            } else {
                throw new TyrionRuntimException("there are different type in the parameters");
            }
        });
        // get rows
        List<String> rows = Lists.newArrayList();
        T t = list.get(0);
        for (Field field : t.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            rows.add(field.getDeclaredAnnotation(Column.class).column());
        }

        // get parameters
        List<Object> parameters = list.stream().filter(Objects::nonNull).flatMap(e -> Arrays
                .stream(e.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Column.class))
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        return f.get(e);
                    } catch (Exception ex) {
                        throw new TyrionRuntimException("get value failed", ex);
                    }
                })
        ).collect(toList());

        SQLParameter sqlParameter = DSL.insert()
                .into(table(t.getClass()))
                .columns(rows.toArray(new String[0]))
                .values(parameters.toArray())
                .build();
        return insert(sqlParameter);
    }

    @Override
    public int update(SQLParameter sqlParameter) {
        return update0(sqlParameter);
    }

    @Override
    public int delete(SQLParameter sqlParameter) {
        return update0(sqlParameter);
    }

    @Override
    public <T> ResultMapHandler<T> getResultMapHandler(List<SelectBuilder.RowMap> rowMaps, Class<T> resultType) {
        return new DefaultResultMapHandler<>(rowMaps, resultType);
    }

    private <R, T> void setParameters(PreparedStatement preparedStatement,
                                      SQLParameter<R, T> sqlParameter) throws SQLException {
        List<Object> parameters = sqlParameter.getParameters();
        if (CollectionUtils.isNotEmpty(parameters)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
        }
    }
}

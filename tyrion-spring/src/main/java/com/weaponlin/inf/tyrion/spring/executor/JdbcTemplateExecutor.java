package com.weaponlin.inf.tyrion.spring.executor;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.annotation.Column;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.executor.Executor;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import com.weaponlin.inf.tyrion.executor.result.ResultMapHandler;
import com.weaponlin.inf.tyrion.spring.executor.result.SpringResultMapHandler;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand.table;
import static java.util.stream.Collectors.toList;

/**
 * JdbcTemplate
 */
public class JdbcTemplateExecutor implements Executor {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateExecutor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public <R, T> R selectOne(SQLParameter<R, T> sqlParameter) {
        List<R> list = selectList(sqlParameter);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 1) {
            throw new TyrionRuntimException("selectOne only require null or one row, actual size: " + list.size());
        }
        return CollectionUtils.isNotEmpty(list) && list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public <R, T> List<R> selectList(SQLParameter<R, T> sqlParameter) {
        return jdbcTemplate.query(sqlParameter.getSql(), sqlParameter.getParameters().toArray(),
                new SpringResultMapHandler<>(sqlParameter.getRowMaps(), sqlParameter.getResultType()));
    }

    @Override
    public int insert(SQLParameter sqlParameter) {
        // batch updates
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

    @Override
    public <T> int insert(T t) {
        return insert(Lists.newArrayList(t));
    }

    /**
     * TODO add sharding id
     *
     * @param list
     * @return
     */
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
                    // TODO refactor
                    Method readMethod = BeanUtils.getPropertyDescriptor(t.getClass(), f.getName()).getReadMethod();
                    try {
                        return readMethod.invoke(e);
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
        return new SpringResultMapHandler<>(rowMaps, resultType);
    }

    private int update0(SQLParameter sqlParameter) {
        return jdbcTemplate.update(sqlParameter.getSql(), sqlParameter.getParameters().toArray());
    }

    private int batchUpdate(SQLParameter sqlParameter, List<Object[]> partitionObjects) {
        return Arrays.stream(jdbcTemplate.batchUpdate(sqlParameter.getSql(), partitionObjects))
                .sum();
    }
}

package com.weaponlin.inf.tyrion.spring.executor.result;

import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.executor.result.ResultMapHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.ResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.adapter.DefaultMappingAdapter;
import com.weaponlin.inf.tyrion.executor.result.mapping.adapter.MappingAdapter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class SpringResultMapHandler<T> implements ResultMapHandler<T>, RowMapper<T> {
    private final List<SelectBuilder.RowMap> rowMaps;
    private final Class<T> resultType;
    private final MappingAdapter mappingAdapter = new DefaultMappingAdapter();
    private final ResultMappingHandler resultMappingHandler;


    public SpringResultMapHandler(final List<SelectBuilder.RowMap> rowMaps, final Class<T> resultType) {
        this.resultType = checkNotNull(resultType, "ResultType can not be null");
        this.resultMappingHandler = mappingAdapter.produce(resultType);
        // map fields to columns
        this.rowMaps = resultMappingHandler.handle(rowMaps, resultType);
    }

    @Override
    public T mapRow(ResultSet rs) {
        return resultMappingHandler.mapToObject(rowMaps, resultType, rs);
    }

    @Override
    public T mapRow(ResultSet resultSet, int index) throws SQLException {
        return mapRow(resultSet);
    }
}

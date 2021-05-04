package com.weaponlin.inf.tyrion.executor.result;

import com.weaponlin.inf.tyrion.executor.result.mapping.ResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.adapter.DefaultMappingAdapter;
import com.weaponlin.inf.tyrion.executor.result.mapping.adapter.MappingAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder.RowMap;

public class DefaultResultMapHandler<T> implements ResultMapHandler<T> {

    private final List<RowMap> rowMaps;
    private final Class<T> resultType;
    private final MappingAdapter mappingAdapter = new DefaultMappingAdapter();
    private final ResultMappingHandler resultMappingHandler;


    public DefaultResultMapHandler(final List<RowMap> rowMaps, final Class<T> resultType) {
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

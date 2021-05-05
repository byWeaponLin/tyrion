package com.weaponlin.inf.tyrion.executor.result;

import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import com.weaponlin.inf.tyrion.executor.result.mapping.ResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.adapter.DefaultMappingAdapter;
import com.weaponlin.inf.tyrion.executor.result.mapping.adapter.MappingAdapter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder.RowMap;

public class DefaultResultMapHandler<R> implements ResultMapHandler<R> {

    private final List<RowMap> rowMaps;
    private final Class<R> resultType;
    private final MappingAdapter mappingAdapter = new DefaultMappingAdapter();
    private final ResultMappingHandler resultMappingHandler;


    public DefaultResultMapHandler(final List<RowMap> rowMaps, final Class<R> resultType) {
        this.resultType = checkNotNull(resultType, "ResultType can not be null");
        this.resultMappingHandler = mappingAdapter.produce(resultType);
        // map fields to columns
        this.rowMaps = resultMappingHandler.handle(rowMaps, resultType);
    }

    @Override
    public R mapRow(ResultSet rs) {
        return resultMappingHandler.mapToObject(rowMaps, resultType, rs);
    }

    @Override
    public List<R> mapRows(ResultSet rs) {
        try {
            List<R> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (Exception e) {
            throw new TyrionRuntimException("map rows failed", e);
        }
    }
}

package com.weaponlin.inf.tyrion.executor.result.mapping;

import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder.RowMap;

public class MapResultMappingHandler implements ResultMappingHandler {

    private MapResultMappingHandler() {
    }

    private static class SingletonHolder {
        private static final MapResultMappingHandler INSTANCE = new MapResultMappingHandler();
    }

    public static ResultMappingHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public <T> List<RowMap> handle(List<RowMap> rowMaps, Class<T> type) {
        return rowMaps;
    }

    @Override
    public <T> T mapToObject(List<RowMap> rowMaps, Class<T> type, ResultSet rs) {
        // TODO
        Map map = new HashMap();
        try {
            for (RowMap rowMap : rowMaps) {
                map.put(Optional.ofNullable(rowMap.getColumn()).orElse(rowMap.getProperty()),
                        rs.getObject(rowMap.getColumn()));
            }
        } catch (Exception e) {
            throw new TyrionRuntimException("Mapping data from column to field failed", e);
        }
        return (T) map;
    }
}

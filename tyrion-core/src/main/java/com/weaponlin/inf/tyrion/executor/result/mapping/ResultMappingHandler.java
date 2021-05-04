package com.weaponlin.inf.tyrion.executor.result.mapping;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import static com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder.RowMap;
import static java.util.stream.Collectors.toMap;

/**
 * mapping result set that from db to Object with Annotation @Table, Pojo, Map, Row Types
 */
public interface ResultMappingHandler {

    <T> List<RowMap> handle(List<RowMap> rowMaps, Class<T> type);

    <T> T mapToObject(List<RowMap> rowMaps, Class<T> type, ResultSet rs);

    default void supplementRowMap(List<RowMap> rowMaps, List<RowMap> allRowMap) {
        Map<String, RowMap> columnMap = allRowMap.stream().collect(toMap(RowMap::getColumn, v -> v));

        rowMaps.forEach(rowMap -> {
            RowMap another = columnMap.get(rowMap.getColumn());
            if (another != null) {
                rowMap.setProperty(another.getProperty());
                rowMap.setJavaType(another.getJavaType());
            }
        });
    }
}

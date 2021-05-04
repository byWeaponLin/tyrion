package com.weaponlin.inf.tyrion.executor.result.mapping;

import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import com.weaponlin.inf.tyrion.executor.result.type.TypeHandler;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.List;

import static com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder.RowMap;

public class RawTypeResultMappingHandler implements ResultMappingHandler {

    private static class SingletonHolder {
        private static final RawTypeResultMappingHandler INSTANCE = new RawTypeResultMappingHandler();
    }

    public static ResultMappingHandler getInstance() {
        return RawTypeResultMappingHandler.SingletonHolder.INSTANCE;
    }

    @Override
    public <T> List<RowMap> handle(List<RowMap> rowMaps, Class<T> type) {
        if (CollectionUtils.isEmpty(rowMaps)) {
            throw new TyrionRuntimException("must select one column because of return basic dataType or String");
        } else if (rowMaps.size() > 1) {
            throw new TyrionRuntimException("only select one column because of return basic dataType or String");
        }
        return rowMaps;
    }

    @Override
    public <T> T mapToObject(List<RowMap> rowMaps, Class<T> resultType, ResultSet rs) {
        try {
            RowMap rowMap = rowMaps.get(0);
            if (resultType.isPrimitive()) {

                Constructor<T> constructor = resultType.getConstructor(resultType);
                return constructor.newInstance(TypeHandler.convert(rowMap.getJavaType(), rs, rowMap.getColumn()));
//                return TypeHandler.convert(rowMap.getJavaType(), rs, rowMap.getColumn());
            } else {
                return (T) TypeHandler.convert(rowMap.getJavaType(), rs, rowMap.getColumn());
            }
        } catch (Exception e) {
            throw new TyrionRuntimException("Mapping data from column to field failed", e);
        }
    }
}

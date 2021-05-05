package com.weaponlin.inf.tyrion.executor.result.mapping;

import com.weaponlin.inf.tyrion.cache.GlobalCache;
import com.weaponlin.inf.tyrion.dsl.meta.TableMeta;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import com.weaponlin.inf.tyrion.executor.result.type.TypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder.RowMap;

@Slf4j
public class OtherResultMappingHandler implements ResultMappingHandler {

    private OtherResultMappingHandler() {
    }

    private static class SingletonHolder {
        private static final OtherResultMappingHandler INSTANCE = new OtherResultMappingHandler();
    }

    public static ResultMappingHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public <T> List<RowMap> handle(List<RowMap> rowMaps, Class<T> type) {
        checkNotNull(type, "type can not bu null");
        TableMeta tableMeta = GlobalCache.getIfNullPut(type);
        List<RowMap> allRowMap = tableMeta.getRowMaps();

        if (CollectionUtils.isEmpty(allRowMap)) {
            throw new TyrionRuntimException("Illegal Class<" + type.getTypeName() + ">"
                    + " does not have any declared fields");
        }

        // compare with selected columns and annotated columns
        if (CollectionUtils.isEmpty(rowMaps)) {
            return allRowMap;
        } else {
            supplementRowMap(rowMaps, allRowMap);
            return rowMaps;
        }
    }

    @Override
    public <T> T mapToObject(List<RowMap> rowMaps, Class<T> resultType, ResultSet rs) {
        try {
            T t = resultType.newInstance();
            for (RowMap rowMap : rowMaps) {
                Object value = TypeHandler.convert(rowMap.getJavaType(), rs, rowMap.getColumn());
                Field field = resultType.getDeclaredField(rowMap.getProperty());
                field.setAccessible(true);
                field.set(t, value);
            }
            return t;
        } catch (Exception e) {
            throw new TyrionRuntimException("Mapping data from column to field failed", e);
        }
    }
}

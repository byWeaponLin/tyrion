package com.weaponlin.inf.tyrion.executor.result.mapping;

import com.weaponlin.inf.tyrion.annotation.Table;
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
public class DefaultResultMappingHandler implements ResultMappingHandler {

    private DefaultResultMappingHandler() {
    }

    private static class SingletonHolder {
        private static final DefaultResultMappingHandler INSTANCE = new DefaultResultMappingHandler();
    }

    public static ResultMappingHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * @param rowMaps
     * @param type    must annotated with {@link Table }
     * @param <T>
     * @return
     */
    @Override
    public <T> List<RowMap> handle(List<RowMap> rowMaps, Class<T> type) {
        checkNotNull(type, "type can not bu null");
        if (!type.isAnnotationPresent(Table.class)) {
            throw new TyrionRuntimException("Illegal type that does not annotated with @Table");
        }

        TableMeta tableMeta = GlobalCache.getIfNullPut(type);
        List<RowMap> allRowMap = tableMeta.getRowMaps();
        if (CollectionUtils.isEmpty(allRowMap)) {
            throw new TyrionRuntimException("Illegal Class<" + type.getTypeName() + ">"
                    + " does not have columns with @Column annotation");
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

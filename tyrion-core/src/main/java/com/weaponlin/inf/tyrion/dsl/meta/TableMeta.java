package com.weaponlin.inf.tyrion.dsl.meta;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.annotation.Column;
import com.weaponlin.inf.tyrion.annotation.Id;
import com.weaponlin.inf.tyrion.annotation.Table;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO 将table元信息缓存起来
 * 1.
 */
@Getter
public class TableMeta {

    private Class clazz;

    private List<String> columns;

    private List<SelectBuilder.RowMap> rowMaps;

    /**
     * 暂不支持复合主键
     */
    private String idColumn;

    public TableMeta(Class entityClass) {
        checkNotNull(entityClass);
        this.clazz = entityClass;
        columns = Lists.newArrayList();
        rowMaps = Lists.newArrayList();
        init(entityClass);
    }

    /**
     * init idColumn, columns, rowMaps
     *
     * @param entityClass
     */
    private void init(Class entityClass) {
        if (entityClass.isAnnotationPresent(Table.class)) {
            initTable(entityClass);
        } else {
            initPojo(entityClass);
        }
    }

    private void initTable(Class clazz) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Column.class))
                .forEach(f -> {
                    Column column = f.getDeclaredAnnotation(Column.class);
                    if (StringUtils.isBlank(idColumn) && f.isAnnotationPresent(Id.class)) {
                        idColumn = column.column();
                    }
                    SelectBuilder.RowMap rowMap = new SelectBuilder.RowMap()
                            .setProperty(f.getName())
                            .setJavaType(f.getType())
                            .setColumn(Optional.of(column.column())
                                    .filter(StringUtils::isNotBlank)
                                    .orElse(f.getName().toLowerCase())
                            );
                    rowMaps.add(rowMap);
                    columns.add(rowMap.getColumn());
                });
    }

    private void initPojo(Class clazz) {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(f -> {
                    SelectBuilder.RowMap rowMap = new SelectBuilder.RowMap()
                            .setProperty(f.getName())
                            .setJavaType(f.getType())
                            .setColumn(f.getName());
                    rowMaps.add(rowMap);
                    columns.add(rowMap.getColumn());
                });
    }
}

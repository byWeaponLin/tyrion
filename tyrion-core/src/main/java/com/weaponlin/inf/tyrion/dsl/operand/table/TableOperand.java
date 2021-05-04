package com.weaponlin.inf.tyrion.dsl.operand.table;

import com.weaponlin.inf.tyrion.annotation.Table;
import com.weaponlin.inf.tyrion.cache.GlobalCache;
import com.weaponlin.inf.tyrion.dsl.meta.TableMeta;
import com.weaponlin.inf.tyrion.dsl.operand.Operand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.sharding.DefaultShardingStrategy;
import com.weaponlin.inf.tyrion.sharding.ShardingStrategy;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO 强约束，添加属性Class tableClass，且必须添加注解{@link Table}
 * 以及对分库分表进行校验
 */
public class TableOperand<T> extends Operand {
    private static final long serialVersionUID = 827621260033371902L;

    private String tableName;

    private String alias;

    @Getter
    private Class<T> entityClazz;

    private ShardingStrategy shardingStrategy = new DefaultShardingStrategy();

    private TableOperand(Class<T> entityClazz, long shardingId) {
        super("");
        this.entityClazz = entityClazz;
        this.tableName = shardingStrategy.sharding(entityClazz, shardingId);
    }

    public static <T> TableOperand<T> table(Class<T> clazz) {
        return table(clazz, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> TableOperand<T> table(Class<T> clazz, long shardingId) {
        return new TableOperand<>(clazz, shardingId);
    }

    public List<ColumnOperand> getColumns() {
        checkNotNull(entityClazz, "entity class can not be null");
        checkArgument(entityClazz.isAnnotationPresent(Table.class), "entity class must annotate with Table");
        TableMeta tableMeta = GlobalCache.getIfNullPut(entityClazz);
        return tableMeta.getColumns().stream()
                .filter(Objects::nonNull)
                .map(ColumnOperand::column)
                .collect(Collectors.toList());
    }

    @Override
    public TableOperand<T> as(String alias) {
        checkArgument(StringUtils.isNotBlank(alias), "alias can not be empty");
        this.alias = alias;
        return this;
    }

    @Override
    protected String decoratedAlias(boolean hasAlias) {
        return (hasAlias && StringUtils.isNotBlank(alias) ? " AS " + alias : "");
    }

    @Override
    public String defaultAlias() {
        return tableName;
    }

    @Override
    public String toString(boolean hasAlias) {
        return tableName + decoratedAlias(hasAlias);
    }

    @Override
    public String toString() {
        return toString(true);
    }
}

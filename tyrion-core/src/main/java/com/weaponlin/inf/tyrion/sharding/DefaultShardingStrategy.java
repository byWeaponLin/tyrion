package com.weaponlin.inf.tyrion.sharding;

import com.weaponlin.inf.tyrion.annotation.Table;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO
 */
public class DefaultShardingStrategy implements ShardingStrategy {

    @Override
    public String sharding(Class clazz, Long shardingId) {
        checkNotNull(clazz, "entity class can not be null");
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new TyrionRuntimException("Illegal entity that without annotation @Table");
        }

        Table table = (Table) clazz.getAnnotation(Table.class);
        checkArgument(StringUtils.isNotBlank(table.database()), "database can not be empty");
        checkArgument(StringUtils.isNotBlank(table.table()), "table can not be empty");
        checkArgument(table.databaseCount() >= 0, "database count can not be negative");
        checkArgument(table.tableCount() >= 0, "table count can not be negative");
        String format = table.database() + "%s%s" + "." + table.table() + "%s%s";
        if (shardingId == null || shardingId <= 0) {
            return String.format(format, "", "", "", "");
        }
        String dbIndex = "";
        String tbIndex = "";
        // TODO 补零 demo_1 ==> demo_01
        if (table.databaseCount() > 0) {
            dbIndex = String.valueOf(shardingId % table.databaseCount());
        }
        if (table.tableCount() > 0) {
            tbIndex = String.valueOf(shardingId % table.tableCount());
        }
        return String.format(format, table.databaseSeparator(), dbIndex, table.tableSeparator(), tbIndex);
    }
}

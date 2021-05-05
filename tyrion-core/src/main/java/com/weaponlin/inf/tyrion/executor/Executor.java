package com.weaponlin.inf.tyrion.executor;


import com.weaponlin.inf.tyrion.cache.GlobalCache;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.builder.SelectBuilder;
import com.weaponlin.inf.tyrion.dsl.meta.TableMeta;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import com.weaponlin.inf.tyrion.executor.result.ResultMapHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * add batchInsert(SQLParameter sqlParameter)
 */
public interface Executor {
    Logger LOGGER = LoggerFactory.getLogger(Executor.class);

    <R, T> R selectOne(SQLParameter<R, T> sqlParameter);

    <R, T> List<R> selectList(SQLParameter<R, T> sqlParameter);

    /**
     * include batch insert
     *
     * @param sqlParameter
     * @return
     */
    int insert(SQLParameter sqlParameter);

    <T> int insert(T t);

    <T> int insert(List<T> list);

    int update(SQLParameter sqlParameter);

    int delete(SQLParameter sqlParameter);

    <T> ResultMapHandler<T> getResultMapHandler(final List<SelectBuilder.RowMap> rowMaps, final Class<T> resultType);

    default void print(SQLParameter sqlParameter) {
        LOGGER.info("[SQL:{}] [PARAMETERS:{}]", sqlParameter.getSql(), sqlParameter.getParameters());
    }

    default <T> String getIdColumn(Class<T> entityClass) {
        checkNotNull(entityClass);
        TableMeta tableMeta = GlobalCache.getIfNullPut(entityClass);
        return Optional.ofNullable(tableMeta.getIdColumn()).filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new TyrionRuntimException("target class must be annotated with @Id and @Column"));
    }
}

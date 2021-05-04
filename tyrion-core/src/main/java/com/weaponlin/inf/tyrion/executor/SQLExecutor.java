package com.weaponlin.inf.tyrion.executor;


import com.weaponlin.inf.tyrion.annotation.Id;
import com.weaponlin.inf.tyrion.cache.GlobalCache;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.meta.TableMeta;
import com.weaponlin.inf.tyrion.executor.exception.TyrionRuntimException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * add batchInsert(SQLParameter sqlParameter)
 */
public interface SQLExecutor {
    Logger LOGGER = LoggerFactory.getLogger(SQLExecutor.class);

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

    /**
     *
     * @param id id column must be annotated with {@link Id}
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> int deleteById(Object id, Class<T> entityClass);

    <T> T getById(Object id, Class<T> entityClass);

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

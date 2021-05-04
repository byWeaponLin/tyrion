package com.weaponlin.inf.tyrion.cache;

import com.weaponlin.inf.tyrion.dsl.meta.TableMeta;

import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TODO IMPORTANT CACHE
 */
public class GlobalCache extends ConcurrentHashMap {

    private GlobalCache() {

    }

    private static final GlobalCache GLOBAL_CACHE = new GlobalCache();

    /**
     * @param clazz
     */
    @Deprecated
    public static void put(Class clazz) {
        GLOBAL_CACHE.put(clazz, new TableMeta(clazz));
    }

    public static TableMeta getIfNullPut(Class clazz) {
        checkNotNull(clazz);
        GLOBAL_CACHE.putIfAbsent(clazz, new TableMeta(clazz));
        return (TableMeta) GLOBAL_CACHE.get(clazz);
    }
}

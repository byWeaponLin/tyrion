package com.weaponlin.inf.tyrion.executor.result.mapping.adapter;

import com.weaponlin.inf.tyrion.executor.result.mapping.ResultMappingHandler;

public interface MappingAdapter {
    ResultMappingHandler produce(Class<?> resultType);
}

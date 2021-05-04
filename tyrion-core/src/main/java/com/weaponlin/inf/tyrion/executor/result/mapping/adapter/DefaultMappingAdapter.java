package com.weaponlin.inf.tyrion.executor.result.mapping.adapter;

import com.google.common.collect.ImmutableSet;
import com.weaponlin.inf.tyrion.annotation.Table;
import com.weaponlin.inf.tyrion.executor.result.mapping.DefaultResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.MapResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.OtherResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.RawTypeResultMappingHandler;
import com.weaponlin.inf.tyrion.executor.result.mapping.ResultMappingHandler;

import java.util.Map;
import java.util.Set;

public class DefaultMappingAdapter implements MappingAdapter {


    private static final Set<Class> BASIC_DATATYPE = ImmutableSet.of(
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class
    );

    @Override
    public ResultMappingHandler produce(Class<?> resultType) {
        if (resultType.isAnnotationPresent(Table.class)) {
            return DefaultResultMappingHandler.getInstance();
        } else if (resultType == Map.class) {
            return MapResultMappingHandler.getInstance();
        } else if (resultType.isPrimitive() || BASIC_DATATYPE.contains(resultType) || resultType.isAssignableFrom(String.class)) {
            // Basic dataType and String
            return RawTypeResultMappingHandler.getInstance();
        } else {
            return OtherResultMappingHandler.getInstance();
        }
    }
}

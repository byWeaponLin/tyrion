package com.weaponlin.inf.tyrion.dsl.operand.transform;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.joining;

public class ValueOperand extends VariableOperand {
    private static final long serialVersionUID = 7109780381078443859L;

    ValueOperand(Object... values) {
        super(values);
    }

    public static VariableOperand values(Object... values) {
        checkNotNull(values, "values can not be null, If only and have a null value, please use new Object[]{null}.");
        checkArgument(values.length > 0, "values's size can not be zero.");
        return new ValueOperand(values);
    }

    public static VariableOperand value(Object value) {
        return values(new Object[]{value});
    }

    /**
     * useless
     *
     * @param hasAlias
     * @return
     */
    @Override
    public String toString(boolean hasAlias) {
        return Optional.ofNullable(parameters)
                .filter(CollectionUtils::isNotEmpty)
                .map(list ->
                        list.stream()
                                .map(e -> Optional.ofNullable(e).map(Object::toString).orElse("NULL"))
                                .collect(joining(", "))
                ).orElse("");
    }

    @Override
    public String defaultAlias() {
        return toString(false);
    }

    @Override
    public String toString() {
        return toString(false);
    }

    @Override
    public List<Object> getParameters() {
        return Collections.emptyList();
    }
}

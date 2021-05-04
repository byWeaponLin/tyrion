package com.weaponlin.inf.tyrion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BooleanOperator {

    AND(" AND "),
    OR(" OR ");

    @Getter
    private String operator;
}

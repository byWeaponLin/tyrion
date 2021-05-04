package com.weaponlin.inf.tyrion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * compare operator
 * GE: >=
 * GT: >
 * EQ: =
 * LE: <=
 * LT: <
 */
@AllArgsConstructor
public enum CompareOperator {
    LT(" < "),
    LE(" <= "),
    EQ(" = "),
    GT(" > "),
    GE(" >= "),
    NEQ(" != "),
    LIKE(" LIKE "),
    NOT_LIKE(" NOT LIKE "),
    IS_NULL(" IS NULL "),
    IS_NOT_NULL(" IS NOT NULL "),
    IN(" IN "),
    NOT_IN(" NOT IN "),
    BETWEEN_AND(" between_and ");

    @Getter
    private String comparator;
}

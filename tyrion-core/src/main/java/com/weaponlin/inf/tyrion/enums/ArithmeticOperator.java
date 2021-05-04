package com.weaponlin.inf.tyrion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ArithmeticOperator {
    ADD(" + "),
    MINUS(" - "),
    MULTIPLY(" * "),
    DIVIDE(" / "),
    AND(" & "),
    OR(" | ");

    @Getter
    private String operator;
}


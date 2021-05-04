package com.weaponlin.inf.tyrion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Order {
    DESC(" DESC"), ASC(" ASC");

    private String order;
}

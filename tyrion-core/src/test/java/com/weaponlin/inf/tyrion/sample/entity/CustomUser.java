package com.weaponlin.inf.tyrion.sample.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomUser {

    private String name;
    private Integer maxAge;
}

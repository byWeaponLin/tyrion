package com.weaponlin.inf.tyrion.dsl.entity;

import com.weaponlin.inf.tyrion.annotation.Column;
import com.weaponlin.inf.tyrion.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(database = "demo", table = "student")
public class Student {

    @Column(column = "id")
    private Long id;

    @Column(column = "name")
    private String name;

    @Column(column = "score")
    private int score;

    @Column(column = "age")
    private int age;
}

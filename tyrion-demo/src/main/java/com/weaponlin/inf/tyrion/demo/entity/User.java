package com.weaponlin.inf.tyrion.demo.entity;

import com.weaponlin.inf.tyrion.annotation.Column;
import com.weaponlin.inf.tyrion.annotation.Id;
import com.weaponlin.inf.tyrion.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(database = "demo", table = "user")
public class User {

    @Id
    @Column(column = "id")
    private Long id;

    @Column(column = "name")
    private String name;

    @Column(column = "gender")
    private String gender;

    @Column(column = "age")
    private Integer age;
}

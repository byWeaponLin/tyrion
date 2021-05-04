package com.weaponlin.inf.tyrion.sample.entity;

import com.weaponlin.inf.tyrion.annotation.Column;
import com.weaponlin.inf.tyrion.annotation.Id;
import com.weaponlin.inf.tyrion.annotation.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(database = "demo", table = "user", databaseCount = 100, tableCount = 1000,
        databaseSeparator = "_", tableSeparator = "_")
public class ShardingUser {

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

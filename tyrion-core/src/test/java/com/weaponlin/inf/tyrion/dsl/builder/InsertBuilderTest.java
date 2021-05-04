package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.entity.Student;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InsertBuilderTest extends BaseTest {
    @Test
    public void test_insert_success() {
        SQLParameter sqlParameter = DSL.insert()
                .into(TableOperand.table(Student.class))
                .columns("id", "name", "score")
                .values(1, "weapon lin", 100)
                .build();
        assertEquals("INSERT INTO demo.student(id, name, score) VALUES(?, ?, ?)", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(1, "weapon lin", 100), sqlParameter.getParameters());
    }

    @Test
    public void test_insert_multiple_columns_and_values_success() {
        SQLParameter sqlParameter = DSL.insert()
                .into(TableOperand.table(Student.class))
                .columns("id", "name")
                .columns("score")
                .values(1)
                .values( "weapon lin", 100)
                .build();
        assertEquals("INSERT INTO demo.student(id, name, score) VALUES(?, ?, ?)", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(1, "weapon lin", 100), sqlParameter.getParameters());
    }

    @Test
    public void test_insert_throw_exception_if_no_columns() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("columns's size must be greater than zero");

        DSL.insert()
                .into(TableOperand.table(Student.class))
                .values(1, "weapon lin", 100)
                .build();
    }

    @Test
    public void test_insert_throw_exception_if_no_values() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("values's size must be greater than zero");

        DSL.insert()
                .into(TableOperand.table(Student.class))
                .columns("id", "name", "score")
                .build();
    }

    @Test
    public void test_insert_throw_exception_if_column_size_not_equal_value_size() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Illegal SQLParameter, columns's size is not equal to values'size, column size = 3,"
                + " value size = 2");
        DSL.insert()
                .into(TableOperand.table(Student.class))
                .columns("id", "name", "score")
                .values(1, "weapon lin")
                .build();
    }
}
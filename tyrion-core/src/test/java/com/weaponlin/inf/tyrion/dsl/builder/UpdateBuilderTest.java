package com.weaponlin.inf.tyrion.dsl.builder;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.entity.Student;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class UpdateBuilderTest extends BaseTest {

    @Test
    public void test_update_success() {
        SQLParameter sqlParameter = DSL.update().table(TableOperand.table(Student.class))
                .set(ColumnOperand.column("score").eq(PlaceholderOperand.value(100)))
                .where()
                .and(ColumnOperand.column("class_id").eq(PlaceholderOperand.value(1)))
                .and(ColumnOperand.column("name").eq(PlaceholderOperand.value("Weapon Lin")))
                .build();
        assertEquals("UPDATE demo.student SET score = ? WHERE class_id = ? AND name = ?", sqlParameter.getSql());
        assertEquals2(Arrays.asList(100, 1, "Weapon Lin"), sqlParameter.getParameters());
    }

    @Test
    public void test_update_without_condition_success() {
        SQLParameter sqlParameter = DSL.update().table(TableOperand.table(Student.class))
                .set(ColumnOperand.column("score").eq(PlaceholderOperand.value(100)))
                .build();
        assertEquals("UPDATE demo.student SET score = ?", sqlParameter.getSql());
        assertEquals2(Arrays.asList(100), sqlParameter.getParameters());
    }

    @Test
    public void test_update_throw_exception_if_table_is_null() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("table can not be null");
        DSL.update().set(ColumnOperand.column("class_id").eq(PlaceholderOperand.value(1))).build();
    }

    @Test
    public void test_update_throw_exception_if_no_assignments() {
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("no assignments, please check sql");
        DSL.update().table(TableOperand.table(Student.class)).build();
    }
}
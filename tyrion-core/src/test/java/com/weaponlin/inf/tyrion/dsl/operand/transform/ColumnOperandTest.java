package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColumnOperandTest extends BaseTest {

    @Test
    public void column_success() {
        ColumnOperand column = ColumnOperand.name("id");
        assertEquals("id", column.toString());
        column.as("_id");
        assertEquals("id AS _id", column.toString());
    }

    @Test
    public void throw_exception_if_column_name_is_null() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("column name can not be empty");
        ColumnOperand.name((String) null);
    }

    @Test
    public void throw_exception_if_column_name_is_empty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("column name can not be empty");
        ColumnOperand.name("");
    }
}
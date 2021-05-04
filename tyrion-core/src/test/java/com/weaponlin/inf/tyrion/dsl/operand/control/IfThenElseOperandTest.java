package com.weaponlin.inf.tyrion.dsl.operand.control;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ValueOperand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IfThenElseOperandTest extends BaseTest {

    @Test
    public void test_if_then_else_success() {
        IfThenElseOperand operand = IfThenElseOperand._if(true).then(ColumnOperand.name("age").gt(ValueOperand.value(10)))
                ._else(ColumnOperand.name("age").lt(ValueOperand.value(10)));
        assertEquals("age > 10", operand.toString(false));

        operand = IfThenElseOperand._if(true).then(ColumnOperand.name("age").gt(ValueOperand.value(10)));
        assertEquals("age > 10", operand.toString(false));

        operand = IfThenElseOperand._if(false).then(ColumnOperand.name("age").gt(ValueOperand.value(10)));
        assertEquals("", operand.toString(false));

        operand = IfThenElseOperand._if(false).then(ColumnOperand.name("age").gt(ValueOperand.value(10)))
                ._else(ColumnOperand.name("age").lt(ValueOperand.value(10)));
        assertEquals("age < 10", operand.toString(false));
    }
}
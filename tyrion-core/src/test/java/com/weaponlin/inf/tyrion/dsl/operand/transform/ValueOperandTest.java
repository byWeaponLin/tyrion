package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import org.junit.Assert;
import org.junit.Test;

public class ValueOperandTest extends BaseTest {

    @Test
    public void test_values_success() {
        VariableOperand values = ValueOperand.values(1, 2, 3);
        Assert.assertEquals("1, 2, 3", values.toString());
    }

    @Test
    public void test_values_success_with_multiple_type_parameters() {
        VariableOperand values = ValueOperand.values(1, 2.1, 3.1f, null, 'a', true);
        Assert.assertEquals("1, 2.1, 3.1, NULL, a, true", values.toString());
    }

    @Test
    public void test_values_throw_exception_if_no_parameters() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("values's size can not be zero.");
        VariableOperand values = ValueOperand.values();
    }

    @Test
    public void test_values_throw_exception_if_parameter_is_null() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("values can not be null, If only and have a null value, please use new Object[]{null}.");
        VariableOperand values = ValueOperand.values();
    }

    @Test
    public void test_values_success_if_has_a_null_value() {
        VariableOperand values = ValueOperand.values(new Object[]{null});
        Assert.assertEquals("NULL", values.toString());
    }

    @Test
    public void test_value_success() {
        VariableOperand value = ValueOperand.value(1);
        Assert.assertEquals("1", value.toString());
    }
}
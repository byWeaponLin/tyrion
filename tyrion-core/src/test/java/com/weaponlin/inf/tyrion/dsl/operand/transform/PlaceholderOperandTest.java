package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PlaceholderOperandTest extends BaseTest {

    @Test
    public void test_values_success() {
        Object[] objects = {1, 2.1, 3.1f, 'a', true};
        VariableOperand values = PlaceholderOperand.values(objects);
        Assert.assertEquals("?, ?, ?, ?, ?", values.toString());
        List<Object> parameters = values.getParameters();
        equals(parameters, objects);
    }

    @Test
    public void test_values_throw_exception_if_no_parameters() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("values's size can not be zero.");
        VariableOperand values = PlaceholderOperand.values();
    }

    @Test
    public void test_values_throw_exception_if_parameter_is_null() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("values's size can not be zero.");
        VariableOperand values = PlaceholderOperand.values();
    }

    @Test
    public void test_values_success_if_has_a_null_value() {
        Object[] objects = {null};
        VariableOperand values = PlaceholderOperand.values(objects);
        Assert.assertEquals("?", values.toString());
        List<Object> parameters = values.getParameters();
        equals(parameters, objects);
    }

    @Test
    public void test_value_success() {
        Object object = 1;
        VariableOperand value = PlaceholderOperand.value(object);
        Assert.assertEquals("?", value.toString());
        List<Object> parameters = value.getParameters();
        equals(parameters, new Object[]{object});
    }
}
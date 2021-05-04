package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import lombok.NonNull;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class ArithmeticOperandTest extends BaseTest {

    @Test
    public void test_arithmetic_with_placeholder_success() {
        ArithmeticOperand operand = ColumnOperand.name("score").add(PlaceholderOperand.value(5));
        assertEquals("score + ?", operand.toString());
        equals(operand, new Object[]{5});

        operand = ColumnOperand.name("score").minus(PlaceholderOperand.value(5));
        assertEquals("score - ?", operand.toString());
        equals(operand, new Object[]{5});

        operand = ColumnOperand.name("score").multiply(PlaceholderOperand.value(5));
        assertEquals("score * ?", operand.toString());
        equals(operand, new Object[]{5});

        operand = ColumnOperand.name("score").divide(PlaceholderOperand.value(5));
        assertEquals("score / ?", operand.toString());
        equals(operand, new Object[]{5});

        operand = ColumnOperand.name("score").and(PlaceholderOperand.value(5));
        assertEquals("score & ?", operand.toString());
        equals(operand, new Object[]{5});

        operand = ColumnOperand.name("score").or(PlaceholderOperand.value(5));
        assertEquals("score | ?", operand.toString());
        equals(operand, new Object[]{5});
    }

    @Test
    public void test_arithmetic_with_value_success() {
        ArithmeticOperand operand = ColumnOperand.name("score").add(ValueOperand.value(5));
        assertEquals("score + 5", operand.toString());

        operand = ColumnOperand.name("score").minus(ValueOperand.value(5));
        assertEquals("score - 5", operand.toString());

        operand = ColumnOperand.name("score").multiply(ValueOperand.value(5));
        assertEquals("score * 5", operand.toString());

        operand = ColumnOperand.name("score").divide(ValueOperand.value(5));
        assertEquals("score / 5", operand.toString());

        operand = ColumnOperand.name("score").and(ValueOperand.value(5));
        assertEquals("score & 5", operand.toString());

        operand = ColumnOperand.name("score").or(ValueOperand.value(5));
        assertEquals("score | 5", operand.toString());
    }

    @Test
    public void test_multiple_arithmetic_success() {
        ArithmeticOperand operand = ColumnOperand.name("score")
                .add(PlaceholderOperand.value(5))
                .minus(PlaceholderOperand.value(10));
        assertEquals("score + ? - ?", operand.toString());
        List<Object> parameters = operand.getParameters();
        equals(parameters, new Object[]{5, 10});
    }

    @Test
    public void test_and_with_function_success() {
        TransformOperand operand = AggregateFunctionOperand.max("score")
                .add(PlaceholderOperand.value(10))
                .minus(ColumnOperand.name("age"))
                .as("what");
        Assert.assertEquals("MAX(score) + ? - age AS what", operand.toString());
        List<Object> parameters = operand.getParameters();
        equals(parameters, new Object[]{10});
    }

    @Test
    public void throw_exception_if_alias_is_empty() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("alias can not be empty");
        ColumnOperand.name("name").add(ValueOperand.value(1)).as("");
    }

    private void equals(@NonNull ArithmeticOperand operand, Object[] objects) {
        equals(operand.getParameters(), objects);
    }
}
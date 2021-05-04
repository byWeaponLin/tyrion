package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.weaponlin.inf.tyrion.dsl.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AggregateFunctionOperandTest extends BaseTest {

    @Test
    public void test_max_success() {
        AggregateFunctionOperand max = AggregateFunctionOperand.max("score");
        assertEquals("MAX(score)", max.toString());
    }

    @Test
    public void test_min_success() {
        TransformOperand min = AggregateFunctionOperand.min("score").as("min_score");
        Assert.assertEquals("MIN(score) AS min_score", min.toString());
    }

    @Test
    public void test_sum_success() {
        TransformOperand sum = AggregateFunctionOperand.sum(ColumnOperand.name("score").as("sre")).as("sum_score");
        Assert.assertEquals("SUM(score) AS sum_score", sum.toString());
    }

    @Test
    public void test_count_throw_exception_if_column_is_blank() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("column can not be blank");
        AggregateFunctionOperand.count("").as("blank");

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("column can not be blank");
        AggregateFunctionOperand.count((String) null).as("blank");
    }

    @Test
    public void test_avg_throw_exception_if_operand_is_null() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("operand can not be null");
        AggregateFunctionOperand.avg((TransformOperand) null).as("null");
    }
}
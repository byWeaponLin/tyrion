package com.weaponlin.inf.tyrion.dsl.operand.transform;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.operand.expression.BetweenExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.CompareExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.InExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.LikeExpressionOperand;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TransformOperandTest extends BaseTest {

    @Test
    public void test_compare() {
        CompareExpressionOperand expression = ColumnOperand.column("id").eq(10);
        assertEquals("id = ?", expression.toString());
        Assert.assertEquals(Lists.newArrayList(10), expression.getParameters());

        expression = ColumnOperand.column("name").eq(PlaceholderOperand.value("weapon"));
        assertEquals("name = ?", expression.toString());
        Assert.assertEquals(Lists.newArrayList("weapon"), expression.getParameters());
    }

    @Test
    public void test_like() {
        LikeExpressionOperand expression = ColumnOperand.column("name").likeBefore("weapon");
        assertEquals("name LIKE ?", expression.toString());
        Assert.assertEquals(Lists.newArrayList("%weapon"), expression.getParameters());

        expression = ColumnOperand.column("name").notLikeMiddle("weapon");
        assertEquals("name NOT LIKE ?", expression.toString());
        Assert.assertEquals(Lists.newArrayList("%weapon%"), expression.getParameters());
    }

    @Test
    public void test_in() {
        InExpressionOperand expression = ColumnOperand.column("age").in(18, 19, 20);
        assertEquals("age IN (?, ?, ?)", expression.toString());
        Assert.assertEquals(Lists.newArrayList(18, 19, 20), expression.getParameters());

        expression = ColumnOperand.column("name").notIn("weapon", "lin");
        assertEquals("name NOT IN (?, ?)", expression.toString());
        Assert.assertEquals(Lists.newArrayList("weapon", "lin"), expression.getParameters());
    }

    @Test
    public void test_betweenAnd() {
        BetweenExpressionOperand expression = ColumnOperand.column("score").betweenAnd(60, 100);
        assertEquals("score BETWEEN ? AND ?", expression.toString());
        Assert.assertEquals(Lists.newArrayList(60, 100), expression.getParameters());
    }

    @Test
    public void test_arithmetic() {
        ArithmeticOperand operand = ColumnOperand.column("age").add(5);
        assertEquals("age + ?", operand.toString());
        Assert.assertEquals(Lists.newArrayList(5), operand.getParameters());
    }
}
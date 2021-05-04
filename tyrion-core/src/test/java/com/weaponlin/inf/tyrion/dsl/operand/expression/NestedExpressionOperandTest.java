package com.weaponlin.inf.tyrion.dsl.operand.expression;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NestedExpressionOperandTest extends BaseTest {

    @Test
    public void test_nested_expression_success() {
        NestedExpressionOperand operand = NestedExpressionOperand.nested()
                .and(ColumnOperand.column("name").likeAfter(PlaceholderOperand.value("林")))
                .and(ColumnOperand.column("score").ge(PlaceholderOperand.value(90)))
                .and(ColumnOperand.column("age").le(PlaceholderOperand.value(16)));

        assertEquals("(name LIKE ? AND score >= ? AND age <= ?)", operand.toString());
        assertEquals2(Lists.newArrayList("林%", 90, 16), operand.getParameters());
    }
}
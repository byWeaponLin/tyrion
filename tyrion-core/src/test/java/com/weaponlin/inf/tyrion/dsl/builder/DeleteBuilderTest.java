package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.entity.Student;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeleteBuilderTest extends BaseTest {

    @Test
    public void test_delete_success() {
        SQLParameter sqlParameter = DSL.delete()
                .from(TableOperand.table(Student.class))
                .where().and(ColumnOperand.name("class_id").eq(PlaceholderOperand.value(1)))
                .and(ColumnOperand.name("name").likeAfter(PlaceholderOperand.value("林")))
                .build();
        assertEquals("DELETE FROM demo.student WHERE class_id = ? AND name LIKE ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(1, "林%"), sqlParameter.getParameters());
    }

    @Test
    public void test_delete_with_between_in_condition_success() {
        SQLParameter sqlParameter = DSL.delete()
                .from(TableOperand.table(Student.class))
                .where().and(ColumnOperand.name("class_id").ge(PlaceholderOperand.value(1)))
                .and(ColumnOperand.name("name").likeAfter(PlaceholderOperand.value("林")))
                .and(ColumnOperand.name("age").in(PlaceholderOperand.values(11, 12, 13)))
                .and(ColumnOperand.name("score").betweenAnd(PlaceholderOperand.value(80), PlaceholderOperand.value(100)))
                .build();
        assertEquals("DELETE FROM demo.student WHERE class_id >= ? AND name LIKE ? AND age IN (?, ?, ?) "
                + "AND score BETWEEN ? AND ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(1, "林%", 11, 12, 13, 80, 100), sqlParameter.getParameters());
    }

    @Test
    public void test_delete_throw_exception_if_no_from() {
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("DeleteBuilder not support this operation");
        DSL.delete().build();
    }
}
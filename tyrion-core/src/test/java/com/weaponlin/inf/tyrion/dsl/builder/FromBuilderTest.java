package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.entity.Student;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.AggregateFunctionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ValueOperand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FromBuilderTest extends BaseTest {
    @Test
    public void test_from_table_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id")
                .column("name")
                .column("score")
                .from(TableOperand.table(Student.class))
                .build();
        assertEquals("SELECT id, name, score FROM demo.student", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());

        sqlParameter = DSL.select().column("id", "name", "score")
                .from(TableOperand.table(Student.class).as("stu"))
                .build();
        assertEquals("SELECT id, name, score FROM demo.student AS stu", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
    }

    @Test
    public void test_transform_operand_from_table_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id")
                .column(ColumnOperand.name("name").as("nm"))
                .column(AggregateFunctionOperand.max("score").as("maxScore"))
                .column(ColumnOperand.name("score").add(ValueOperand.value(10)).as("newScore"))
                .column(ColumnOperand.name("name").likeMiddle(PlaceholderOperand.value("林")))
                .column(ColumnOperand.name("age").gt(ValueOperand.value(15)))
                .column(ColumnOperand.name("name").likeAfter(ValueOperand.value("林")))
                .from(TableOperand.table(Student.class))
                .build();
        assertEquals("SELECT id, name AS nm, MAX(score) AS maxScore, score + 10 AS newScore, "
                + "name LIKE ?, age > 15, name LIKE '林%' FROM demo.student", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList("%林%"), sqlParameter.getParameters());
    }
}
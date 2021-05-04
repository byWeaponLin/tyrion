package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.entity.Student;
import com.weaponlin.inf.tyrion.dsl.operand.control.IfThenElseOperand;
import com.weaponlin.inf.tyrion.dsl.operand.expression.NestedExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.AggregateFunctionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ValueOperand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectBuilderTest extends BaseTest {

    @Test
    public void test_select_with_nested_expression_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name")
                .from(TableOperand.table(Student.class))
                .where()
                .and(ColumnOperand.column("score").ge(PlaceholderOperand.value(90)))
                .and(NestedExpressionOperand.nested()
                        .or(ColumnOperand.column("name").likeAfter(PlaceholderOperand.value("林")))
                        .or(ColumnOperand.column("age").le(PlaceholderOperand.value(16)))
                )
                .limit(10)
                .build();
        assertEquals("SELECT id, name FROM demo.student WHERE score >= ? AND (name LIKE ? OR age <= ?) LIMIT ?",
                sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(90, "林%", 16, 10), sqlParameter.getParameters());
    }

    @Test
    public void test_select_only_has_nested_expression() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name")
                .from(TableOperand.table(Student.class))
                .where()
                .and(NestedExpressionOperand.nested()
                        .or(ColumnOperand.column("name").likeAfter(PlaceholderOperand.value("林")))
                        .or(ColumnOperand.column("age").le(PlaceholderOperand.value(16)))
                )
                .build();
        assertEquals("SELECT id, name FROM demo.student WHERE (name LIKE ? OR age <= ?)", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList("林%", 16), sqlParameter.getParameters());
    }

    @Test
    public void test_select_with_empty_nested_expression() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name")
                .from(TableOperand.table(Student.class))
                .where()
                .and(ColumnOperand.column("score").ge(PlaceholderOperand.value(90)))
                .and(NestedExpressionOperand.nested())
                .limit(10)
                .build();
        assertEquals("SELECT id, name FROM demo.student WHERE score >= ? LIMIT ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(90, 10), sqlParameter.getParameters());
    }

    @Test
    public void select_limit_without_conditions() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name")
                .from(TableOperand.table(Student.class))
                .where().limit(10)
                .build();
        assertEquals("SELECT id, name FROM demo.student LIMIT ?", sqlParameter.getSql());
    }

    @Test
    public void test_select_with_if_then_else_success() {
        SQLParameter sqlParameter = DSL.select().column("id").column(IfThenElseOperand._if(true).then(ColumnOperand.name("age"))).build();
        assertEquals("SELECT id, age", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "age"), getColumns(sqlParameter));

        sqlParameter = DSL.select().column("id").column(IfThenElseOperand._if(true).then(ColumnOperand.name("age").gt(PlaceholderOperand.value(10)))).build();
        assertEquals("SELECT id, age > ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "age > 10"), getColumns(sqlParameter));
    }

    @Test
    public void test_select_with_if_false_but_no_else_success() {
        SQLParameter sqlParameter = DSL.select().column("id")
                .column(IfThenElseOperand._if(false).then(ColumnOperand.name("age")))
                .build();
        assertEquals("SELECT id", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id"), getColumns(sqlParameter));
    }

    @Test
    public void test_select_with_if_false_success() {
        SQLParameter sqlParameter = DSL.select().column("id")
                .column(IfThenElseOperand._if(false).then(ColumnOperand.name("age"))._else(ColumnOperand.name("age").gt(PlaceholderOperand.value(10)).as("what")))
                .build();
        assertEquals("SELECT id, age > ? AS what", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "what"), getColumns(sqlParameter));
    }

    @Test
    public void test_string_columns_success() {
        SQLParameter sqlParameter = DSL.select().column("id").column("name").column("score").build();
        assertEquals("SELECT id, name, score", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "name", "score"), getColumns(sqlParameter));

        sqlParameter = DSL.select().column("id", "name", "score").build();
        assertEquals("SELECT id, name, score", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "name", "score"), getColumns(sqlParameter));
    }

    @Test
    public void test_column_operand_success() {
        SQLParameter sqlParameter = DSL.select()
                .column(ColumnOperand.name("id")).column(ColumnOperand.name("name").as("nm")).column(ColumnOperand.name("score"))
                .build();
        assertEquals("SELECT id, name AS nm, score", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "nm", "score"), getColumns(sqlParameter));

        sqlParameter = DSL.select().column(ColumnOperand.name("id"), ColumnOperand.name("name").as("nm"), ColumnOperand.name("score")).build();
        assertEquals("SELECT id, name AS nm, score", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "nm", "score"), getColumns(sqlParameter));
    }

    @Test
    public void test_mix_string_and_operand_columns_success() {
        SQLParameter sqlParameter = DSL.select().column("id").column(ColumnOperand.name("name").as("nm")).column("score").build();
        assertEquals("SELECT id, name AS nm, score", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "nm", "score"), getColumns(sqlParameter));
    }

    @Test
    public void test_transform_operand_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id")
                .column(ColumnOperand.name("name").as("nm"))
                .column(AggregateFunctionOperand.max("score").as("maxScore"))
                .column(ColumnOperand.name("score").add(ValueOperand.value(10)).as("newScore"))
                .column(ColumnOperand.name("name").likeAfter(PlaceholderOperand.value("林")))
                .column(ColumnOperand.name("age").gt(ValueOperand.value(15)))
                .column(ColumnOperand.name("name").likeAfter(ValueOperand.value("林")))
                .build();
        assertEquals("SELECT id, name AS nm, MAX(score) AS maxScore, score + 10 AS newScore, "
                + "name LIKE ?, age > 15, name LIKE '林%'", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList("林%"), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("id", "nm", "maxScore", "newScore", "name LIKE 林%", "age > 15",
                "name LIKE '林%'"), getColumns(sqlParameter));
    }

    @Test
    public void test_select_with_expression_alias_success() {
        SQLParameter sqlParameter = DSL.select()
                .column(AggregateFunctionOperand.max("score").as("max").ge(ValueOperand.values(90)).as("maxScore"))
                .build();
        assertEquals("SELECT MAX(score) >= 90 AS maxScore", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("maxScore"), getColumns(sqlParameter));

        sqlParameter = DSL.select().column(AggregateFunctionOperand.max("score").as("max")).build();
        assertEquals("SELECT MAX(score) AS max", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("max"), getColumns(sqlParameter));
    }

    @Test
    public void test_select_like_alias_success() {
        SQLParameter sqlParameter = DSL.select().column(ColumnOperand.name("name").likeAfter(PlaceholderOperand.value("林")).as("TheSameName")).build();
        assertEquals("SELECT name LIKE ? AS TheSameName", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList("林%"), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("TheSameName"), getColumns(sqlParameter));
    }

    @Test
    public void test_select_in_alias_success() {
        SQLParameter sqlParameter = DSL.select().column(ColumnOperand.name("age").in(PlaceholderOperand.values(11, 12, 13)).as("TheSameAge")).build();
        assertEquals("SELECT age IN (?, ?, ?) AS TheSameAge", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(11, 12, 13), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("TheSameAge"), getColumns(sqlParameter));

        sqlParameter = DSL.select().column(ColumnOperand.name("age").in(ValueOperand.values(11, 12, 13)).as("TheSameAge")).build();
        assertEquals("SELECT age IN (11, 12, 13) AS TheSameAge", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
        assertEquals2(Lists.newArrayList("TheSameAge"), getColumns(sqlParameter));
    }
}
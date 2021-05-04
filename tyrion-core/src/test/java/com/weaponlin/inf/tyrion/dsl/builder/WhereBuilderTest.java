package com.weaponlin.inf.tyrion.dsl.builder;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.BaseTest;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.entity.Student;
import com.weaponlin.inf.tyrion.dsl.operand.control.IfThenElseOperand;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.AggregateFunctionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WhereBuilderTest extends BaseTest {

    @Test
    public void test_where_with_goup_by_having_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name", "score")
                .from(TableOperand.table(Student.class))
                .where()
                .orderBy("score")
                .desc()
                .limit(10)
                .build();
        assertEquals("SELECT id, name, score FROM demo.student ORDER BY score DESC LIMIT ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10), sqlParameter.getParameters());
    }

    @Test
    public void test_where_with_group_by_havaing_count_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name", "score")
                .from(TableOperand.table(Student.class))
                .where()
                .groupBy("score")
                .having(AggregateFunctionOperand.count("score").gt(PlaceholderOperand.value(90)))
                .orderBy("score")
                .asc()
                .limit(10)
                .build();
        assertEquals("SELECT id, name, score FROM demo.student GROUP BY score HAVING COUNT(score) > ? "
                + "ORDER BY score ASC LIMIT ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(90, 10), sqlParameter.getParameters());
    }

    @Test
    public void test_where_with_if_then_else_success() {
        String name = "";
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name", "score")
                .from(TableOperand.table(Student.class))
                .where()
                .and(ColumnOperand.name("score").ge(PlaceholderOperand.value(90)))
                .and(IfThenElseOperand._if(StringUtils.isNotBlank(name))
                        .then(ColumnOperand.column("name").likeMiddle(PlaceholderOperand.value("林")))
                        ._else(ColumnOperand.column("name").likeAfter(PlaceholderOperand.value("林"))))
                .build();
        assertEquals("SELECT id, name, score FROM demo.student WHERE score >= ? AND name LIKE ?",
                sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(90, "林%"), sqlParameter.getParameters());
    }

    @Test
    public void test_where_common_comparator_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("id", "name", "score")
                .from(TableOperand.table(Student.class))
                .where().and(ColumnOperand.name("score").ge(PlaceholderOperand.value(90)))
                .and(ColumnOperand.name("name").likeAfter(PlaceholderOperand.value("林")))
                .build();
        assertEquals("SELECT id, name, score FROM demo.student WHERE score >= ? AND name LIKE ?",
                sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(90, "林%"), sqlParameter.getParameters());
    }

    @Test
    public void test_where_like_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("name")
                .from(TableOperand.table(Student.class))
                .where()
                .and(ColumnOperand.name("name").likeAfter(PlaceholderOperand.value("林")))
                .and(ColumnOperand.name("age").notLikeAfter(PlaceholderOperand.value(2)))
                .and(ColumnOperand.name("class_id").like(PlaceholderOperand.value(1)))
                .build();
        assertEquals("SELECT name FROM demo.student WHERE name LIKE ? AND age NOT LIKE ? AND class_id LIKE ?",
                sqlParameter.getSql());
        assertEquals2(Lists.newArrayList("林%", "2%", "1"), sqlParameter.getParameters());
    }

    @Test
    public void test_where_in_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("name")
                .from(TableOperand.table(Student.class))
                .where()
                .or(ColumnOperand.name("age").notIn(PlaceholderOperand.values(15, 16, 17)))
                .or(ColumnOperand.name("score").in(PlaceholderOperand.values(80, 90)))
                .or(ColumnOperand.name("class_id").in(PlaceholderOperand.values(1, 2, 3)))
                .build();
        assertEquals("SELECT name FROM demo.student WHERE age NOT IN (?, ?, ?) OR score IN (?, ?) OR "
                + "class_id IN (?, ?, ?)", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(15, 16, 17, 80, 90, 1, 2, 3), sqlParameter.getParameters());
    }

    @Test
    public void test_where_between_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("name")
                .from(TableOperand.table(Student.class))
                .where()
                .and(ColumnOperand.name("age").betweenAnd(PlaceholderOperand.value(13), PlaceholderOperand.value(15)))
                .build();
        assertEquals("SELECT name FROM demo.student WHERE age BETWEEN ? AND ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(13, 15), sqlParameter.getParameters());
    }

    @Test
    public void test_where_group_by_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("class_id")
                .column(AggregateFunctionOperand.count("class_id").as("classPerson"))
                .from(TableOperand.table(Student.class))
                .where()
                .groupBy("class_id")
                .build();
        assertEquals("SELECT class_id, COUNT(class_id) AS classPerson FROM demo.student GROUP BY class_id",
                sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(), sqlParameter.getParameters());
    }

    @Test
    public void test_where_limit_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("class_id")
                .from(TableOperand.table(Student.class))
                .where()
                .limit(10)
                .build();
        assertEquals("SELECT class_id FROM demo.student LIMIT ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10), sqlParameter.getParameters());

        sqlParameter = DSL.select()
                .column("class_id")
                .from(TableOperand.table(Student.class))
                .where()
                .limit(10, 20)
                .build();
        assertEquals("SELECT class_id FROM demo.student LIMIT ?, ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10, 20), sqlParameter.getParameters());
    }

    @Test
    public void test_where_group_by_limit_success() {
        SQLParameter sqlParameter = DSL.select()
                .column("score")
                .from(TableOperand.table(Student.class))
                .where()
                .groupBy("score")
                .limit(10)
                .build();
        assertEquals("SELECT score FROM demo.student GROUP BY score LIMIT ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10), sqlParameter.getParameters());

        sqlParameter = DSL.select()
                .column("score")
                .from(TableOperand.table(Student.class))
                .where()
                .groupBy("score")
                .limit(10, 10)
                .build();
        assertEquals("SELECT score FROM demo.student GROUP BY score LIMIT ?, ?", sqlParameter.getSql());
        assertEquals2(Lists.newArrayList(10, 10), sqlParameter.getParameters());
    }
}
package com.weaponlin.inf.tyrion.executor.sample;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.expression.NestedExpressionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.AggregateFunctionOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand;
import com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand;
import com.weaponlin.inf.tyrion.executor.SQLExecutor;
import com.weaponlin.inf.tyrion.sample.entity.CustomUser;
import com.weaponlin.inf.tyrion.sample.entity.ShardingUser;
import com.weaponlin.inf.tyrion.sample.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test_applicationContext.xml"})
public class DefaultSpringSQLExecutorTest {

    @Autowired
    private SQLExecutor sqlExecutor;

    @Test
    public void test_insert_object() {
        User user = new User();
        user.setId(322L);
        user.setGender("male");
        user.setName("weaponbaba");
        user.setAge(19);
        int result = sqlExecutor.insert(user);
        assertEquals(1, result);

        SQLParameter sqlParameter = DSL.delete().from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").eq(PlaceholderOperand.value(322)))
                .build();
        sqlExecutor.delete(sqlParameter);
    }

    @Test
    public void test_batch_insert_object() {
        List<User> lists = Lists.newArrayList(
                new User().setId(66L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(77L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(88L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(99L).setAge(10).setName("weapon").setGender("male")
        );
        int result = sqlExecutor.insert(lists);
        assertEquals(4, result);

        SQLParameter sqlParameter = DSL.delete()
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").in(PlaceholderOperand.values(66, 77, 88, 99)))
                .build();
        result = sqlExecutor.delete(sqlParameter);
        assertEquals(4, result);
    }

    @Test
    public void test_select_list_with_nested() {
        SQLParameter<User, User> sqlParameter = DSL.<User, User>select()
                .column("id", "name", "gender")
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").gt(PlaceholderOperand.value(0)))
                .and(NestedExpressionOperand.nested()
                        .or(ColumnOperand.column("name").likeMiddle(PlaceholderOperand.value("weapon")))
                        .or(ColumnOperand.column("id").gt(PlaceholderOperand.value(0)))
                )
                .build();
        List<User> users = sqlExecutor.selectList(sqlParameter);
        System.out.println(users);
    }

    @Test
    public void test_select_list() {
        SQLParameter<User, User> sqlParameter = DSL.<User, User>select()
                .column("id", "name", "gender")
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").gt(PlaceholderOperand.value(0)))
                .and(ColumnOperand.column("name").likeMiddle(PlaceholderOperand.value("weapon")))
                .build();
        List<User> users = sqlExecutor.selectList(sqlParameter);
        System.out.println(users);
    }

    @Test
    public void select_all_test() {
        SQLParameter<User, User> sqlParameter = DSL.<User, User>select()
                .columns()
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").gt(PlaceholderOperand.value(0)))
                .and(ColumnOperand.column("name").likeMiddle(PlaceholderOperand.value("weapon")))
                .build();
        List<User> users = sqlExecutor.selectList(sqlParameter);
        System.out.println(users);
    }

    @Test
    public void select_max_test() {
        SQLParameter<Integer, User> sqlParameter = DSL.<Integer, User>select().column(AggregateFunctionOperand.max("age"))
                .from(TableOperand.table(User.class))
                .build(Integer.class);
        System.out.println(sqlExecutor.selectOne(sqlParameter));
    }

    @Test
    public void select_string_test() {
        SQLParameter<String, User> sqlParameter = DSL.<String, User>select()
                .column("name")
                .from(TableOperand.table(User.class))
                .where()
                .limit(1)
                .build(String.class);
        System.out.println(sqlExecutor.selectOne(sqlParameter));
    }

    @Test
    public void select_custom_result_type_test() {
        SQLParameter<CustomUser, User> sqlParameter = DSL.<CustomUser, User>select()
                .column("name")
                .column(AggregateFunctionOperand.max("age").as("maxAge"))
                .from(TableOperand.table(User.class))
                .where()
                .groupBy(ColumnOperand.column("name"))
                .build(CustomUser.class);
        List<CustomUser> customUsers = sqlExecutor.selectList(sqlParameter);
        System.out.println(customUsers);
    }

    @Test
    public void select_map_test() {
        SQLParameter<Map, User> sqlParameter = DSL.<Map, User>select()
                .column("name")
                .column(AggregateFunctionOperand.max("age").as("maxAge"))
                .from(TableOperand.table(User.class))
                .where()
                .groupBy(ColumnOperand.column("name"))
                .build(Map.class);
        List<Map> customUsers = sqlExecutor.selectList(sqlParameter);
        System.out.println(customUsers);
    }

//    @Test
//    public void test_select_list_with_sharding() {
//        long shardingId = 5555555;
//        SQLParameter<ShardingUser, ShardingUser> sqlParameter = DSL.<ShardingUser, ShardingUser>select()
//                .column("id", "name", "gender", "age")
//                .from(TableOperand.table(ShardingUser.class, shardingId))
//                .where()
//                .and(ColumnOperand.column("id").gt(PlaceholderOperand.value(0)))
//                .build();
//        List<ShardingUser> users = sqlExecutor.selectList(sqlParameter);
//        System.out.println(users);
//    }

    @Test
    public void test_insert() {
        SQLParameter sqlParameter = DSL.insert().into(TableOperand.table(User.class))
                .columns("id", "name", "gender", "age")
                .values(2, "weaponbaba", "m", 24)
                .build();
        int result = sqlExecutor.insert(sqlParameter);
        assertEquals(1, result);
    }

    @Test
    public void test_update() {
        SQLParameter sqlParameter = DSL.update().table(TableOperand.table(User.class))
                .set(ColumnOperand.column("name").eq(PlaceholderOperand.value("weaponbaba2")))
                .where()
                .and(ColumnOperand.column("id").eq(PlaceholderOperand.value(2)))
                .build();
        int result = sqlExecutor.update(sqlParameter);
        assertEquals(1, result);
    }

    @Test
    public void test_delete() {
        SQLParameter sqlParameter = DSL.delete().from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").eq(PlaceholderOperand.value(2)))
                .build();
        int result = sqlExecutor.delete(sqlParameter);
    }

}
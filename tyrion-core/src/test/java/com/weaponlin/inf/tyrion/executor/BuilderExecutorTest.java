package com.weaponlin.inf.tyrion.executor;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.datasource.PooledDatasource;
import com.weaponlin.inf.tyrion.sample.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand.table;
import static com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand.column;
import static com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand.value;
import static com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand.values;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BuilderExecutorTest {

    private BuilderExecutor builderExecutor;

    @Before
    public void setUp() throws Exception {
        PooledDatasource pooledDatasource = new PooledDatasource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root",
                "weaponlin"
        );
        Executor executor = new DefaultExecutor(pooledDatasource);
        builderExecutor = new BuilderExecutor(executor);
    }

    @Test
    public void test_select() {
        List<User> users = builderExecutor.<User, User>select()
                .columns()
                .from(table(User.class))
                .fetch();
        System.out.println(users);
    }

    @Test
    public void query_null_for_data_not_exist() {
        User user = builderExecutor.<User, User>select()
                .columns()
                .from(table(User.class))
                .where()
                .and(column("id").eq(-1))
                .fetchOne();
        assertNull(user);
    }

    @Test
    public void test_insert() {
        int rows = builderExecutor.insert()
                .into(table(User.class))
                .columns("id", "name", "age", "gender")
                .values(99, "weapon", 22, "male")
                .exec();
        assertEquals(1, rows);

        rows = builderExecutor.delete().from(table(User.class))
                .where()
                .and(column("id").eq(value(99)))
                .exec();
        assertEquals(1, rows);
    }

    @Test
    public void test_direct_insert() {
        User user = new User().setId(66L).setAge(10).setName("weapon").setGender("male");
        int result = builderExecutor.insert(user);
        assertEquals(1, result);

        result = builderExecutor.delete()
                .from(table(User.class))
                .where()
                .and(column("id").eq(value(66)))
                .exec();
        assertEquals(1, result);
    }

    @Test
    public void test_direct_batch_insert() {
        List<Object> lists = Lists.newArrayList(
                new User().setId(66L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(77L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(88L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(99L).setAge(10).setName("weapon").setGender("male")
        );
        int result = builderExecutor.batchInsert(lists);
        assertEquals(4, result);

        result = builderExecutor.delete()
                .from(table(User.class))
                .where()
                .and(column("id").in(values(66, 77, 88, 99)))
                .exec();
        assertEquals(4, result);
    }

}
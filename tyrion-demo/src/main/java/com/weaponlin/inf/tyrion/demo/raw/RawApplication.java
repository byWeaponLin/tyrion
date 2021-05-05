package com.weaponlin.inf.tyrion.demo.raw;

import com.google.common.collect.Lists;
import com.weaponlin.inf.tyrion.datasource.PooledDatasource;
import com.weaponlin.inf.tyrion.demo.dao.UserDao;
import com.weaponlin.inf.tyrion.demo.dao.impl.UserDaoImpl;
import com.weaponlin.inf.tyrion.demo.entity.User;
import com.weaponlin.inf.tyrion.executor.DefaultExecutor;
import com.weaponlin.inf.tyrion.executor.Executor;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RawApplication {

    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        PooledDatasource pooledDatasource = new PooledDatasource(
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://localhost:3306/demo?characterEncoding=utf8&useSSL=false&serverTimezone=UTC",
                "root",
                "weaponlin"
        );
        Executor executor = new DefaultExecutor(pooledDatasource);
        userDao = new UserDaoImpl(executor);
    }

    @Test
    public void get_all_user() {
        List<User> users = userDao.getAllUser();
        System.out.println(users);
    }

    @Test
    public void add_user() {
        User user = new User().setId(66L).setAge(10).setName("weapon").setGender("male");

        int affectedRows = userDao.addUser(user);
        assertEquals(1, affectedRows);

        User u = userDao.getUser(66L);
        assertNotNull(u);
        assertEquals(66L, u.getId().longValue());
        assertEquals(10, u.getAge().intValue());
        assertEquals("weapon", u.getName());
        assertEquals("male", u.getGender());

        affectedRows = userDao.deleteUser(66L);
        assertEquals(1, affectedRows);

        u = userDao.getUser(66L);
        assertNull(u);
    }

    @Test
    public void add_users() {
        List<User> lists = Lists.newArrayList(
                new User().setId(66L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(77L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(88L).setAge(10).setName("weapon").setGender("male"),
                new User().setId(99L).setAge(10).setName("weapon").setGender("male")
        );
        int affectedRows = userDao.addUsers(lists);
        assertEquals(4, affectedRows);

        affectedRows = userDao.deleteUsers(Lists.newArrayList(66L,
                77L, 88L, 99L));
        assertEquals(4, affectedRows);

        List<User> users = userDao.getUsers(Lists.newArrayList(66L,
                77L, 88L, 99L));

        assertTrue(CollectionUtils.isEmpty(users));
    }

    @Test
    public void modify_name() {
        User user = new User().setId(66L).setAge(10).setName("weapon").setGender("male");

        int affectedRows = userDao.addUser(user);
        assertEquals(1, affectedRows);

        User u = userDao.getUser(66L);
        assertNotNull(u);
        assertEquals(66L, u.getId().longValue());
        assertEquals(10, u.getAge().intValue());
        assertEquals("weapon", u.getName());
        assertEquals("male", u.getGender());

        affectedRows = userDao.modUserName(66L, "unknown");

        assertEquals(1, affectedRows);

        u = userDao.getUser(66L);
        assertNotNull(u);
        assertEquals(66L, u.getId().longValue());
        assertEquals(10, u.getAge().intValue());
        assertEquals("unknown", u.getName());
        assertEquals("male", u.getGender());

        affectedRows = userDao.deleteUser(66L);
        assertEquals(1, affectedRows);

        u = userDao.getUser(66L);
        assertNull(u);
    }
}

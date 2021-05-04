package com.weaponlin.inf.tyrion.executor;

//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:test_applicationContext.xml"})
public class DefaultSQLExecutorTest {
/*
    @Autowired
    @Qualifier("defaultSQLExecutor")
    private DefaultSQLExecutor executor;

    @Test
    public void test_select() {
        List<User> users = executor.<User, User>select()
                .columns()
                .from(TableOperand.table(User.class))
                .fetch();
        System.out.println(users);
    }

    @Test
    public void test_insert() {
        int rows = executor.insert()
                .into(TableOperand.table(User.class))
                .columns("id", "name", "age", "gender")
                .values(99, "weapon", 22, "male")
                .exec();
        assertEquals(1, rows);

        rows = executor.delete().from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").eq(PlaceholderOperand.value(99)))
                .exec();
        assertEquals(1, rows);
    }

    @Test
    public void test_direct_insert() {
        User user = new User().setId(66L).setAge(10).setName("weapon").setGender("male");
        int result = executor.insert(user);
        assertEquals(1, result);

        result = executor.delete()
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").eq(PlaceholderOperand.value(66)))
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
        int result = executor.batchInsert(lists);
        assertEquals(4, result);

        result = executor.delete()
                .from(TableOperand.table(User.class))
                .where()
                .and(ColumnOperand.column("id").in(PlaceholderOperand.values(66, 77, 88, 99)))
                .exec();
        assertEquals(4, result);
    }

    @Test
    public void test_get_by_id_and_delete_by_id() {
        User user = new User().setId(66L).setAge(10).setName("weapon").setGender("male");
        int result = executor.insert(user);
        assertEquals(1, result);

        User selectUser = executor.getById(66, User.class);
        assertEquals("weapon", selectUser.getName());
        assertEquals("male", selectUser.getGender());
        assertEquals(10, selectUser.getAge().intValue());

        result = executor.deleteById(66, User.class);
        assertEquals(1, result);

        selectUser = executor.getById(66, User.class);
        assertNull(selectUser);
    }

 */
}
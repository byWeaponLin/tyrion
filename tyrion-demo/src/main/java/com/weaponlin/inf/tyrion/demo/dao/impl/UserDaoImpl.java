package com.weaponlin.inf.tyrion.demo.dao.impl;


import com.weaponlin.inf.tyrion.demo.dao.UserDao;
import com.weaponlin.inf.tyrion.demo.entity.User;
import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.executor.Executor;
import lombok.NonNull;

import java.util.List;

import static com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand.table;
import static com.weaponlin.inf.tyrion.dsl.operand.transform.ColumnOperand.column;
import static com.weaponlin.inf.tyrion.dsl.operand.transform.PlaceholderOperand.values;


public class UserDaoImpl implements UserDao {

    private final Executor executor;

    public UserDaoImpl(@NonNull Executor executor) {
        this.executor = executor;
    }


    @Override
    public List<User> getAllUser() {
        SQLParameter sqlParameter = DSL.select()
                .from(table(User.class))
                .build();

        return executor.selectList(sqlParameter);
    }

    @Override
    public int addUser(User user) {
        return executor.insert(user);
    }

    @Override
    public User getUser(Long id) {
        SQLParameter<User, User> sqlParameter = DSL.<User, User>select()
                .from(table(User.class))
                .where()
                .and(column("id").eq(id))
                .build();

        return executor.selectOne(sqlParameter);
    }

    @Override
    public List<User> getUsers(List<Long> ids) {
        SQLParameter<User, User> sqlParameter = DSL.<User, User>select()
                .from(table(User.class))
                .where()
                .and(column("id").in(ids))
                .build();

        return executor.selectList(sqlParameter);
    }

    @Override
    public int modUserName(Long id, String name) {
        SQLParameter sqlParameter = DSL.update().table(User.class)
                .set(column("name").eq(name))
                .where()
                .and(column("id").eq(id))
                .build();

        return executor.update(sqlParameter);
    }

    @Override
    public int addUsers(List<User> users) {
        return executor.insert(users);
    }

    @Override
    public int deleteUser(Long id) {
        SQLParameter sqlParameter = DSL.delete().from(table(User.class))
                .where()
                .and(column("id").eq(id))
                .build();
        return executor.delete(sqlParameter);
    }

    @Override
    public int deleteUsers(List<Long> ids) {
        SQLParameter sqlParameter = DSL.delete().from(table(User.class))
                .where()
                .and(column("id").in(values(ids)))
                .build();
        return executor.delete(sqlParameter);
    }
}

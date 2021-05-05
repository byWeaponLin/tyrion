package com.weaponlin.inf.tyrion.spring.executor.sample.dao.impl;

import com.weaponlin.inf.tyrion.spring.executor.sample.dao.UserDao;
import com.weaponlin.inf.tyrion.spring.executor.sample.entity.User;

import java.util.List;


public class UserDaoImpl implements UserDao {

//    Executor executor = new JdbcTemplateExecutor();

    @Override
    public List<User> getAllUser() {
//        SQLParameter sqlParameter = DSL.select()
//                .from(TableOperand.table(User.class))
//                .build();
//        return executor.selectList(sqlParameter);
        return null;
    }
}

package com.weaponlin.inf.tyrion.sample.dao.impl;

import com.weaponlin.inf.tyrion.sample.dao.UserDao;
import com.weaponlin.inf.tyrion.sample.entity.User;

import java.util.List;


public class UserDaoImpl implements UserDao {

//    Executor executor = new DefaultSpringExecutor();

    @Override
    public List<User> getAllUser() {
//        SQLParameter sqlParameter = DSL.select()
//                .from(TableOperand.table(User.class))
//                .build();
//        return executor.selectList(sqlParameter);
        return null;
    }
}

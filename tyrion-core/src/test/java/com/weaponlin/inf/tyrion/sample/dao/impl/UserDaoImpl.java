package com.weaponlin.inf.tyrion.sample.dao.impl;

import com.weaponlin.inf.tyrion.dsl.DSL;
import com.weaponlin.inf.tyrion.dsl.SQLParameter;
import com.weaponlin.inf.tyrion.dsl.operand.table.TableOperand;
import com.weaponlin.inf.tyrion.executor.DefaultSpringSQLExecutor;
import com.weaponlin.inf.tyrion.executor.SQLExecutor;
import com.weaponlin.inf.tyrion.sample.dao.UserDao;
import com.weaponlin.inf.tyrion.sample.entity.User;

import java.util.List;


public class UserDaoImpl implements UserDao {

    SQLExecutor sqlExecutor = new DefaultSpringSQLExecutor();

    @Override
    public List<User> getAllUser() {
        SQLParameter sqlParameter = DSL.select()
                .from(TableOperand.table(User.class))
                .build();
        return sqlExecutor.selectList(sqlParameter);
    }
}

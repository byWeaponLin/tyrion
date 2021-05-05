package com.weaponlin.inf.tyrion.demo.dao;


import com.weaponlin.inf.tyrion.demo.entity.User;

import java.util.List;

public interface UserDao {
    List getAllUser();

    int addUser(User user);

    User getUser(Long id);

    List<User> getUsers(List<Long> ids);

    int modUserName(Long id, String name);

    int addUsers(List<User> users);

    int deleteUser(Long id);

    int deleteUsers(List<Long> ids);
}

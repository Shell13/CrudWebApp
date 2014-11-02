package com.dao;

import com.model.User;

import java.util.List;

public interface UserDao {
    boolean saveOrUpdateUser(User user);
    boolean deleteUser(User user);
    User getUser(int Id);
    User getLast();
    List getAllUsers();
}

package com.dao;

import com.model.User;

import java.util.List;

public interface UserDao {
    void saveOrUpdateUser(User user);
    void deleteUser(User user);
    User getUser(int Id);
    User getLast();
    List getAllUsers();
}

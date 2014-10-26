package com.dao;

import com.model.User;

import java.util.List;

/**
 * Created by Roman on 21.10.2014.
 */
public interface UserDaoInterface {
    boolean saveOrUpdateUser(User user);
    boolean deleteUser(User user);
    User getUser(int Id);
    List getAllUsers();
}

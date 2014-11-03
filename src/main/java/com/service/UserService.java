package com.service;

import com.dao.UserDao;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Roman on 03.11.2014.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean save(User user) {
        return userDao.saveOrUpdateUser(user);
    }

    public boolean deleteUser(User user) {
        return userDao.deleteUser(user);
    }

    public User getUser(int id) {
        return userDao.getUser(id);
    }

    public User getLast() {
        return userDao.getLast();
    }

    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        return userDao.getAllUsers();
    }
}

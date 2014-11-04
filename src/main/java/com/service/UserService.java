package com.service;

import com.dao.UserDao;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public void saveOrUpdate(User user) {
        userDao.saveOrUpdateUser(user);
    }

    @Transactional
    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    @Transactional
    public User getUser(int id) {
        return userDao.getUser(id);
    }

    @Transactional
    public User getLast() {
        return userDao.getLast();
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<User> getAll() {
        return userDao.getAllUsers();
    }
}

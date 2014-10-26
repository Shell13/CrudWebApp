package com;

import com.dao.UserDao;
import com.model.User;

import java.util.ArrayList;

/**
 * Created by Roman on 25.10.2014.
 */
public class Main {
    public static void main(String[] args) {

        UserDao userDao = new UserDao();

        ArrayList<User> list = (ArrayList<User>) userDao.getAllUsers();

        for (User user : list) {
            System.out.println(user);
        }

        userDao.deleteUser(userDao.getUser(3));
        list = (ArrayList<User>) userDao.getAllUsers();
        for (User user : list) {
            System.out.println(user);
        }
    }
}

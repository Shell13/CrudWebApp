package com.utils;

import com.dao.UserDao;
import com.model.User;

import java.util.Random;

/**
 * Created by Roman on 28.10.2014.
 */
// Наполнение БД списком юзеров
public class FillingDB {
    private static UserDao userDao = new UserDao();

    private static String[] names = {"Leonard", "Alice", "Sheldon", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Fry", "Umberto", "Lila", "Bender",
            "Lisa", "Marge"
    };

    private static User generateUser() {
        Random random = new Random();
        User user = new User();
        user.setName(names[(int) (names.length * Math.random())]); // select random name
        user.setAge(Math.abs(random.nextInt(125)));
        user.setAdmin(random.nextBoolean());
        return user;
    }

    // fill 2 pages at page length - 24
    public static void fillDB() {  //todo
        for (int i = 0; i < 48; i++) {
            userDao.saveOrUpdateUser(generateUser());
        }
    }
}

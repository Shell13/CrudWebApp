package com.utils;

import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Component
public class FillingDB {

    @Autowired
    private UserService userService;

    private String[] names = {"Leonard", "Alice", "Sheldon", "Mike", "Olivia",
            "Nina", "Alex", "Rita", "Fry", "Umberto", "Lila", "Bender",
            "Lisa", "Marge"
    };

    private User generateUser() {
        Random random = new Random();
        User user = new User();
        user.setName(names[(int) (names.length * Math.random())]); // select random name
        user.setAge(Math.abs(random.nextInt(125)));
        user.setAdmin(random.nextBoolean());
        return user;
    }

    // fill 2 pages at page length - 24
    @Transactional
    public void fillDB() {  //todo
        for (int i = 0; i < 48; i++) {
            userService.saveOrUpdate(generateUser());
        }
    }
}

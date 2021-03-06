package com.controller;

import com.logger.LoggerWrapper;
import com.model.User;
import com.service.UserService;
import com.vaadin.data.util.IndexedContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserContainer extends IndexedContainer{

    private static final LoggerWrapper LOG = LoggerWrapper.get(UserContainer.class);

    @Autowired
    private UserService userService;

    @SuppressWarnings("unchecked")
    public IndexedContainer initContainer() {

        LOG.info("initContainer");

        for (String p : Columns.COLUMNS) {
            addContainerProperty(p, String.class, "");
        }

        List<User> users = userService.getAll();
        for (User user : users) {
            Object id = addItem();
            getContainerProperty(id, Columns.ID).setValue(String.valueOf(user.getId()));
            getContainerProperty(id, Columns.NAME).setValue(user.getName());
            getContainerProperty(id, Columns.AGE).setValue(String.valueOf(user.getAge()));
            getContainerProperty(id, Columns.IS_ADMIN).setValue(String.valueOf(user.isAdmin()));
            getContainerProperty(id, Columns.CREATED_DATE).setValue(String.valueOf(user.getCratedDate()));
        }
        return this;
    }
}

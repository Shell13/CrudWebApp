package com.controller;

import com.logger.LoggerWrapper;
import com.utils.FillingDB;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.view.UserTableLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Theme("mytheme")
@SuppressWarnings("serial")
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserUI extends UI {

    private static final LoggerWrapper LOG = LoggerWrapper.get(UserUI.class);

    @Autowired
    private FillingDB fillDB;

    @Autowired
    private UserTableLayout userTableLayout;

    @Override
    protected void init(VaadinRequest request) {

        LOG.info("init");

//        fillDB.fillDB(); //TODO
        userTableLayout.initLayout();
        setContent(userTableLayout);
    }
}

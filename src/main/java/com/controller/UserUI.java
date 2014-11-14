package com.controller;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Theme("mytheme")
@SuppressWarnings("serial")
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserUI extends UI {

//    @Autowired
//    private FillingDB fillDB;

    @Autowired
    private UserContainer userContainer;

    @Autowired
    private UserTableLayout userTableLayout;

//    @Autowired
//    private EditLayout editLayout;

//    @Autowired
//    private SearchField searchField;

//    @Autowired
//    private UserTable userTable;

//    @Autowired
//    private NewUserWindow newUserWindow;

//    @Autowired
//    private PagingPanel pagingPanel;

    //    {
//
//        fillDB.fillDB(); //TODO
//    }
    boolean flag = true;

    @Override
    protected void init(VaadinRequest request) {

        userContainer.initContainer();
        userTableLayout.initLayout();
//        userTable.initUserTable();
//        editLayout.initEditor();
//        editLayout.initRemoveSaveButtons();
//        newUserWindow.initNewUserWindow();
//        searchField.initSearch();
//        pagingPanel.initPaging();
        setContent(userTableLayout);
    }
}

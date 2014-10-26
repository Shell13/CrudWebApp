package com.controller;

//import org.springframework.stereotype.Controller;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Roman on 21.10.2014.
 */
//@Controller
public class UserController extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = UserController.class)
    public static class Servlet extends VaadinServlet {

    }

    private Table contactList = new Table();
    private TextField searchField = new TextField();
    private Button addNewContactButton = new Button("New");
    private Button removeContactButton = new Button("Remove this contact");
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();

    private static final String USER_ID = "ID";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final String IS_ADMIN = "Admin";
    private static final String CREATED_DATE = "Date";

    private static final String[] fieldNames = new String[] {USER_ID, NAME,
            AGE, IS_ADMIN, CREATED_DATE};

    @Override
    protected void init(VaadinRequest request) {
        initLayout();
//        initContactList();
//        initEditor();
//        initSearch();
//        initAddRemoveButtons();
    }
//    protected void init(VaadinRequest request) {
//        final VerticalLayout layout = new VerticalLayout();
//        layout.setMargin(true);
//        setContent(layout);
//
//        Button button = new Button("Click Me");
//        button.addClickListener(new Button.ClickListener() {
//            public void buttonClick(Button.ClickEvent event) {
//                layout.addComponent(new Label("Thank you for clicking"));
//            }
//        });
//        layout.addComponent(button);
//
//        // ********************************************************************************************
//        //
//        // Using postgresql with these data in pom.xml file:
//        //
//		/*<dependency>
//			<groupId>org.hibernate</groupId>
//			<artifactId>hibernate-core</artifactId>
//			<version>4.2.6.Final</version>
//		</dependency>
//		<dependency>
//	    	<groupId>postgresql</groupId>
//	    	<artifactId>postgresql</artifactId>
//	    	<version>9.0-801.jdbc4</version>
//		</dependency>*/
//
////        User user = new User();
////        user.setId(1);
////        user.setName("The very first user)");
////
////        // Getting a Session object:
////        Configuration configuration = new Configuration();
////        configuration.configure();
////        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
////        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
////        Session session = sessionFactory.openSession();
////
////        // Using session object to save an object.
////        // First, begin a transaction:
////        session.beginTransaction();
////
////        // Saving:
////        session.save(user);
////
////        // Ending the transaction:
////        session.getTransaction().commit();
//        // ************************************************************************************************
//    }

    private void initLayout() {

		/* Root of the user interface component tree is set */
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        setContent(splitPanel);

		/* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(contactList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewContactButton);

		/* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the contactList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
        leftLayout.setExpandRatio(contactList, 1);
        contactList.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewContactButton. The height of the layout is defined
		 * by the tallest component.
		 */
        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);

		/* Put a little margin around the fields in the right side editor */
        editorLayout.setMargin(true);
        editorLayout.setVisible(false);
    }

}

package com.controller;

//import org.springframework.stereotype.Controller;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Button button = new Button("Click Me");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                layout.addComponent(new Label("Thank you for clicking"));
            }
        });
        layout.addComponent(button);

        // ********************************************************************************************
        //
        // Using postgresql with these data in pom.xml file:
        //
		/*<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>4.2.6.Final</version>
		</dependency>
		<dependency>
	    	<groupId>postgresql</groupId>
	    	<artifactId>postgresql</artifactId>
	    	<version>9.0-801.jdbc4</version>
		</dependency>*/

//        User user = new User();
//        user.setId(1);
//        user.setName("The very first user)");
//
//        // Getting a Session object:
//        Configuration configuration = new Configuration();
//        configuration.configure();
//        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        Session session = sessionFactory.openSession();
//
//        // Using session object to save an object.
//        // First, begin a transaction:
//        session.beginTransaction();
//
//        // Saving:
//        session.save(user);
//
//        // Ending the transaction:
//        session.getTransaction().commit();
        // ************************************************************************************************
    }
}

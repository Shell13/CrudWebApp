package com.controller;

//import org.springframework.stereotype.Controller;

import com.model.User;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.hbnutil.HbnContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
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

    private Table userList = new Table();
    private TextField searchField = new TextField();
    private Button addNewUserButton = new Button("New");
    private Button removeUserButton = new Button("Remove");
    private Button saveUserButton = new Button("Save");
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();

    private static final String USER_ID = "ID";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final String IS_ADMIN = "Admin";
    private static final String CREATED_DATE = "Date";

    private static final String[] fieldNames = new String[] {USER_ID, NAME,
            AGE, IS_ADMIN, CREATED_DATE};

    private Table table;
    /*
         * Any component can be bound to an external data source. This example uses
         * just a dummy in-memory list, but there are many more practical
         * implementations.
         */
    IndexedContainer contactContainer = table.setContainerDataSource(new HbnContainer(User.class, this));

    @Override
    protected void init(VaadinRequest request) {
        initLayout();
        initContactList();
        initEditor();
        initSearch();
        initAddRemoveButtons();
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
        leftLayout.addComponent(userList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewUserButton);

		/* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

		/*
		 * On the left side, expand the size of the userList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
        leftLayout.setExpandRatio(userList, 1);
        userList.setSizeFull();

		/*
		 * In the bottomLeftLayout, searchField takes all the width there is
		 * after adding addNewUserButton. The height of the layout is defined
		 * by the tallest component.
		 */
        bottomLeftLayout.setWidth("100%");
        searchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(searchField, 1);

		/* Put a little margin around the fields in the right side editor */
        editorLayout.setMargin(true);
        editorLayout.setVisible(false);
    }

    private void initEditor() {

        editorLayout.addComponent(removeUserButton);

		/* User interface can be created dynamically to reflect underlying data. */
        for (String fieldName : fieldNames) {
            TextField field = new TextField(fieldName);
            editorLayout.addComponent(field);
            field.setWidth("100%");

			/*
			 * We use a FieldGroup to connect multiple components to a data
			 * source at once.
			 */
            editorFields.bind(field, fieldName);
        }

		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
        editorFields.setBuffered(false);
    }

    private void initAddRemoveButtons() {
        addNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

				/*
				 * Rows in the Container data model are called Item. Here we add
				 * a new row in the beginning of the list.
				 */
                contactContainer.removeAllContainerFilters();
                Object contactId = contactContainer.addItemAt(0);

				/*
				 * Each Item has a set of Properties that hold values. Here we
				 * set a couple of those.
				 */
                userList.getContainerProperty(contactId, USER_ID).setValue(
                        "New");
                userList.getContainerProperty(contactId, NAME).setValue(
                        "Name");

				/* Lets choose the newly created contact to edit it. */
                userList.select(contactId);
            }
        });

        removeUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object userId = userList.getValue();
                userList.removeItem(userId);
            }
        });
    }

    private void initSearch() {

		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
        searchField.setInputPrompt("Search contacts");

		/*
		 * Granularity for sending events over the wire can be controlled. By
		 * default simple changes like writing a text in TextField are sent to
		 * server with the next Ajax call. You can set your component to be
		 * immediate to send the changes to server immediately after focus
		 * leaves the field. Here we choose to send the text over the wire as
		 * soon as user stops writing for a moment.
		 */
        searchField.setTextChangeEventMode(AbstractTextField.TextChangeEventMode.LAZY);

		/*
		 * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
        searchField.addTextChangeListener(new FieldEvents.TextChangeListener() {
            public void textChange(final FieldEvents.TextChangeEvent event) {

				/* Reset the filter for the contactContainer. */
                contactContainer.removeAllContainerFilters();
                contactContainer.addContainerFilter(new ContactFilter(event
                        .getText()));
            }
        });
    }

    private void initContactList() {
        userList.setContainerDataSource(contactContainer);
        userList.setVisibleColumns(new String[]{USER_ID, NAME, AGE});
        userList.setSelectable(true);
        userList.setImmediate(true);

        userList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                Object contactId = userList.getValue();

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
                if (contactId != null)
                    editorFields.setItemDataSource(userList
                            .getItem(contactId));

                editorLayout.setVisible(contactId != null);
            }
        });
    }

    private class ContactFilter implements Container.Filter {
        private String needle;

        public ContactFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        public boolean passesFilter(Object itemId, Item item) {
            String haystack = ("" + item.getItemProperty(USER_ID).getValue()
                    + item.getItemProperty(NAME).getValue() + item
                    .getItemProperty(AGE).getValue()).toLowerCase();
            return haystack.contains(needle);
        }

        public boolean appliesToProperty(Object id) {
            return true;
        }
    }

    /*
	 * Generate some in-memory example data to play with. In a real application
	 * we could be using SQLContainer, JPAContainer or some other to persist the
	 * data.
	 */
    private static IndexedContainer createDummyDatasource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

		/* Create dummy data by randomly combining first and last names */
        String[] fnames = { "Peter", "Alice", "Joshua", "Mike", "Olivia",
                "Nina", "Alex", "Rita", "Dan", "Umberto", "Henrik", "Rene",
                "Lisa", "Marge" };
        String[] lnames = { "Smith", "Gordon", "Simpson", "Brown", "Clavel",
                "Simons", "Verne", "Scott", "Allison", "Gates", "Rowling",
                "Barks", "Ross", "Schneider", "Tate" };
        for (int i = 0; i < 100; i++) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, USER_ID).setValue(
                    fnames[(int) (fnames.length * Math.random())]);
            ic.getContainerProperty(id, NAME).setValue(
                    lnames[(int) (lnames.length * Math.random())]);
        }

        return ic;
    }
}

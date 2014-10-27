package com.controller;

//import org.springframework.stereotype.Controller;

import com.dao.UserDao;
import com.model.User;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;
import java.util.List;

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
    private TextField nameField = new TextField();
    private TextField ageField = new TextField();
    private TextField isAdminField = new TextField();
    private Button addNewUserButton = new Button("New");
    private Button removeUserButton = new Button("Remove");
    private Button saveUserButton = new Button("Save");
    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();
    private CheckBox checkBox = new CheckBox("Admin", false);

    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final String IS_ADMIN = "Admin";
    private static final String CREATED_DATE = "Date";

    private static final String[] fieldNames = new String[] {ID, NAME,
            AGE, IS_ADMIN, CREATED_DATE};

    private static final String[] leftFields = new String[]{NAME, AGE};

    private static UserDao userDao = new UserDao();
    /*
         * Any component can be bound to an external data source. This example uses
         * just a dummy in-memory list, but there are many more practical
         * implementations.
         */
//    Container contactContainer = getContainer();
    IndexedContainer contactContainer = createDummyDatasource();

    @Override
    protected void init(VaadinRequest request) {
        initLayout();
        initContactList();
        initEditor();
        initSearch();
        initAddRemoveButtons();
    }

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

//        TextField idField = new TextField();


        TextField nameField = new TextField();
        nameField.setInputPrompt("Enter name");
        nameField.setMaxLength(25);
        editorLayout.addComponent(nameField);


        TextField ageField = new TextField();
        ageField.setInputPrompt("Enter age");
        ageField.setMaxLength(3);
        editorLayout.addComponent(ageField);


		/* User interface can be created dynamically to reflect underlying data. */
//        for (String fieldName : leftFields) {
//            TextField field = new TextField(fieldName);
//            editorLayout.addComponent(field);
//            field.setWidth("100%");
//
//			/*
//			 * We use a FieldGroup to connect multiple components to a data
//			 * source at once.
//			 */
//            editorFields.bind(field, fieldName);
//        }
        CheckBox checkBox = new CheckBox("Admin", false);

        checkBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(final Property.ValueChangeEvent event) {
                final String valueString = String.valueOf(event.getProperty()
                        .getValue());
                Notification.show("Value changed:", valueString,
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });

        editorLayout.addComponent(checkBox);

        editorLayout.addComponent(saveUserButton);

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
                Object contactId = contactContainer.addItem(0);


				/*
				 * Each Item has a set of Properties that hold values. Here we
				 * set a couple of those.
				 */
                nameField.setInputPrompt("Enter name");
                ageField.setInputPrompt("Enter age");
                checkBox.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(final Property.ValueChangeEvent event) {
                        final String valueString = String.valueOf(event.getProperty()
                                .getValue());
                        Notification.show("Value changed:", valueString,
                                Notification.Type.TRAY_NOTIFICATION);
                    }
                });

                userList.getContainerProperty(nameField, NAME).setValue("Name");
                userList.getContainerProperty(ageField, AGE).setValue("");
                userList.getContainerProperty(checkBox, IS_ADMIN).setValue("false");

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
        searchField.setInputPrompt("Search users");

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
        userList.setVisibleColumns(new String[]{ID, NAME, AGE, IS_ADMIN, CREATED_DATE});
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
            String haystack = ("" + item.getItemProperty(ID).getValue()
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

        List<User> users = userDao.getAllUsers();
        for (User user : users){
            Object id = ic.addItem();
            ic.getContainerProperty(id, ID).setValue(String.valueOf(user.getId()));
            ic.getContainerProperty(id, NAME).setValue(user.getName());
            ic.getContainerProperty(id, AGE).setValue(String.valueOf(user.getAge()));
            ic.getContainerProperty(id, IS_ADMIN).setValue(String.valueOf(user.isAdmin()));
            ic.getContainerProperty(id, CREATED_DATE).setValue(String.valueOf(user.getCratedDate()));
        }
        return ic;
    }
}

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
import java.sql.Date;
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

    static {
//        FillingDB.fillDB(); //todo
    }

    private Table userList = new Table();
    private TextField searchField = new TextField();
    private Window window = new Window("New User");

    private TextField nameField = new TextField();
    private TextField ageField = new TextField();
    private CheckBox checkBox = new CheckBox("Admin", false);

    private TextField windowNameField = new TextField("Name");
    private TextField windowAgeField = new TextField("Age");
    private CheckBox windowCheckBox = new CheckBox("Admin", false);

    private Button addNewUserButton = new Button("New");
    private Button removeUserButton = new Button("Remove");
    private Button saveOldUserButton = new Button("Save");
    private Button saveNewUserButton = new Button("Save");
    private Button cancelButton = new Button("Cancel");

    private FormLayout editorLayout = new FormLayout();
    private FormLayout windowLayout = new FormLayout(); //???
    private FieldGroup editorFields = new FieldGroup();

    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final String IS_ADMIN = "Admin";
    private static final String CREATED_DATE = "Date";

    private static final String[] fieldNames = new String[]{ID, NAME,
            AGE, IS_ADMIN, CREATED_DATE};

    private static final String[] leftFields = new String[]{NAME, AGE};//TODO

    private static UserDao userDao = new UserDao();
    /*
         * Any component can be bound to an external data source. This example uses
         * just a dummy in-memory list, but there are many more practical
         * implementations.
         */
//    Container userContainer = getContainer();
    IndexedContainer userContainer = readDatasource();

    @Override
    protected void init(VaadinRequest request) {
        initLayout();
        initContactList();
        initEditor();
        initSearch();
        initAddRemoveSaveCanselButtons();
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

    private void initEditor() {  //TODO

        editorLayout.addComponent(removeUserButton);

		/* User interface can be created dynamically to reflect underlying data. */


        TextField editNameField = new TextField(NAME);
        editNameField.setMaxLength(25);

        TextField editAgeField = new TextField(AGE);
        editAgeField.setMaxLength(3);


        CheckBox editIsAdmin = new CheckBox(IS_ADMIN);

        editorLayout.addComponent(editNameField);
        editorLayout.addComponent(editAgeField);
        editorLayout.addComponent(editIsAdmin);

        editorFields.bind(editNameField, NAME);
        editorFields.bind(editAgeField, AGE);
        editorFields.bind(editIsAdmin, IS_ADMIN);

        editorLayout.addComponent(saveOldUserButton);
		/*
		 * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
        editorFields.setBuffered(false);
    }

    private void initAddRemoveSaveCanselButtons() { //TODO

        addNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                HorizontalLayout windowLeftLayout = new HorizontalLayout();
                windowLeftLayout.setSizeFull();
                windowLeftLayout.addStyleName("outlined");

                window.setContent(windowLeftLayout);
                window.setWidth(300.0f, Unit.PIXELS); // размер окна
                window.center();  // размещение по центру
                window.setModal(true); // пока открыто окно, задний план не доступен
                window.setResizable(false);  // изменение размера
                window.setDraggable(false);  // перетаскивание

                FormLayout content = new FormLayout();
                window.setContent(content);
                UI.getCurrent().addWindow(window);
                windowLeftLayout.addComponent(saveNewUserButton);
                windowLeftLayout.addComponent(cancelButton);

				userContainer.removeAllContainerFilters();


				/*
				 * Each Item has a set of Properties that hold values. Here we
				 * set a couple of those.
				 */
                windowNameField.setInputPrompt("Enter name");
                windowAgeField.setInputPrompt("Enter age");


                content.addComponent(windowNameField);
                content.addComponent(windowAgeField);
                content.addComponent(windowCheckBox);
                content.addComponent(windowLeftLayout);
            }
        });

        saveOldUserButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Object userId = userList.getValue();

                Item item = userList.getItem(userId);
                String str[] = item.toString().split(" ");
                int id = Integer.parseInt(str[0]);
                String name = str[1];
                int age = Integer.parseInt(str[2]);
                boolean isAdmin = Boolean.parseBoolean(str[3]);
                System.out.println(isAdmin);

                User oldUser = userDao.getUser(id);
                oldUser.setName(name);
                oldUser.setAge(age);
                oldUser.setAdmin(isAdmin);
                userDao.saveOrUpdateUser(oldUser);

                Notification.show("User saved!",  //todo
                        Notification.Type.TRAY_NOTIFICATION);
            }
        });


        saveNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                windowNameField.setInputPrompt("Enter name");
                windowNameField.setMaxLength(25);
                windowLayout.addComponent(windowNameField);

                windowAgeField.setInputPrompt("Enter age");
                windowAgeField.setMaxLength(3);
                windowLayout.addComponent(windowAgeField); //todo

                String name = windowNameField.getValue();
                int age = Integer.parseInt(windowAgeField.getValue());
                boolean isAdmin = windowCheckBox.getValue();

                User user = new User();
                user.setName(name);
                user.setAge(age);
                user.setAdmin(isAdmin);

                if (userDao.saveOrUpdateUser(user)) {
                    Notification.show("New user added!",
                            Notification.Type.TRAY_NOTIFICATION);
                }

                // Создаю строку для нового юзера и заполняюю ее данными
                Object newUserId = userContainer.addItem();
                Item row = userContainer.getItem(newUserId);

                row.getItemProperty(ID).setValue("00");
                row.getItemProperty(NAME).setValue(name);
                row.getItemProperty(AGE).setValue(String.valueOf(age));
                row.getItemProperty(IS_ADMIN).setValue(String.valueOf(isAdmin));
//                row.getItemProperty(IS_ADMIN).setValue(isAdmin == true ? "Yes" : "No");
                row.getItemProperty(CREATED_DATE).setValue(String.valueOf(new Date(System.currentTimeMillis())));

                // очищаю поля
                windowNameField.setValue("");
                windowAgeField.setValue("");
                windowCheckBox.setValue(false);
                window.close();
            }
        });


        removeUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object userId = userList.getValue();

                /* Из таблицы userList получаю item (содержит данные из строки)
                 * С помощью split() получаю массив строк, где первый элемент
                 * это id юзера, которого нужно удалить из БД
                 */
                Item item = userList.getItem(userId);

                String str[] = item.toString().split(" "); //todo
                int id = Integer.parseInt(str[0]);

                try {
                    userDao.deleteUser(userDao.getUser(id));
                    Notification.show("Removed",
                            Notification.Type.TRAY_NOTIFICATION);
                } catch (Exception e) {
                    Notification
                            .show("Remove failed: " + e.getCause().getMessage(),
                                    Notification.Type.TRAY_NOTIFICATION);
                }
                userList.removeItem(userId);
            }
        });

        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                windowNameField.setValue("");
                windowAgeField.setValue("");
                windowCheckBox.setValue(false);
                window.close();
            }
        });

    }

    private void initSearch() {

		/*
		 * We want to show a subtle prompt in the search field. We could also
		 * set a caption that would be shown above the field or description to
		 * be shown in a tooltip.
		 */
        searchField.setInputPrompt("Search users");//TODO

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

				/* Reset the filter for the userContainer. */
                userContainer.removeAllContainerFilters();
                userContainer.addContainerFilter(new ContactFilter(event
                        .getText()));
            }
        });
    }

    private void initContactList() {
        userList.setContainerDataSource(userContainer);
        userList.setVisibleColumns(new String[]{ID, NAME, AGE, IS_ADMIN, CREATED_DATE});
        userList.setSelectable(true);
        userList.setImmediate(true);

        userList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Object userId = userList.getValue();
//                Item item = userList.getItem(userId);
//                System.out.println(item);
//                String str[] = item.toString().split(" ");
//                boolean isAdmin = str[3].equals("Yes");//
//                checkBox.setValue(isAdmin);

				/*
				 * When a contact is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our contact at once.
				 */
                if (userId != null) {
                    editorFields.setItemDataSource(userList.getItem(userId));
                }
                editorLayout.setVisible(userId != null);
            }
        });
    }

    private class ContactFilter implements Container.Filter { //TODO
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

    private static IndexedContainer readDatasource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            Object id = ic.addItem();
            ic.getContainerProperty(id, ID).setValue(String.valueOf(user.getId()));
            ic.getContainerProperty(id, NAME).setValue(user.getName());
            ic.getContainerProperty(id, AGE).setValue(String.valueOf(user.getAge()));
            ic.getContainerProperty(id, IS_ADMIN).setValue(String.valueOf(user.isAdmin())); // == true ? "Yes" : "No");
            ic.getContainerProperty(id, CREATED_DATE).setValue(String.valueOf(user.getCratedDate()));
        }
        return ic;
    }
}

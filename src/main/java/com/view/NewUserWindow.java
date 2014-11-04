package com.view;

import com.controller.Columns;
import com.model.User;
import com.service.UserService;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Component
public class NewUserWindow extends Window{

    @Autowired
    private UserService userService;

    @Autowired
    private UserTable userTable;

    @Autowired
    private IndexedContainer userContainer;

    private Window newUserWindow = new Window("New User");

    private TextField windowNameField = new TextField("Name");
    private TextField windowAgeField = new TextField("Age");

    private CheckBox windowCheckBox = new CheckBox("Admin", false);

    private Button saveNewUserButton = new Button("Save");
    private Button cancelButton = new Button("Cancel");

    private int getTotalAmountOfPages() {
        int size = userContainer.size();
        int pageLength = userTable.getPageLength();
        int pageCount;
        if (size % pageLength == 0) {
            pageCount = size / pageLength;
        } else pageCount = size / pageLength + 1;

        return pageCount;
    }

    public Window getNewUserWindow(){
        HorizontalLayout windowLayout = new HorizontalLayout();
        windowLayout.setSizeFull();
        windowLayout.addStyleName("outlined");

        newUserWindow.setContent(windowLayout);
        newUserWindow.setWidth(300.0f, Unit.PIXELS); // Window size
        newUserWindow.center();  // Sets this newUserWindow to be centered
        newUserWindow.setModal(true); // Sets newUserWindow modality
        newUserWindow.setResizable(false);  // change the size
        newUserWindow.setDraggable(false);  // newUserWindow can be dragged

        FormLayout content = new FormLayout();
        newUserWindow.setContent(content);
        UI.getCurrent().addWindow(newUserWindow);
        windowLayout.addComponent(saveNewUserButton);
        windowLayout.addComponent(cancelButton);

        userContainer.removeAllContainerFilters();

        windowNameField.setInputPrompt("Enter name");
        windowNameField.setMaxLength(25);
        windowAgeField.setInputPrompt("Enter age");
        windowAgeField.setMaxLength(3);

        content.addComponent(windowNameField);
        content.addComponent(windowAgeField);
        content.addComponent(windowCheckBox);
        content.addComponent(windowLayout);

        return newUserWindow;
    }

    @SuppressWarnings("unchecked")
    public void initNewUserWindow(){
        saveNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                String name = windowNameField.getValue().trim();
                if (name.equals("")) {
                    Notification.show("Warning!", "Enter name!", Notification.Type.WARNING_MESSAGE);
                    newUserWindow.close();
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(windowAgeField.getValue());
                } catch (NumberFormatException e) {
                    Notification.show("Warning!", "Enter digits!", Notification.Type.WARNING_MESSAGE);
                    newUserWindow.close();
                    return;
                }

                boolean isAdmin = windowCheckBox.getValue();

                User user = new User();
                user.setName(name);
                user.setAge(age);
                user.setAdmin(isAdmin);

                userService.saveOrUpdate(user);
                Notification.show("New user added!", Notification.Type.TRAY_NOTIFICATION);

                User last = userService.getLast();
                int id = last.getId();
                String date = String.valueOf(last.getCratedDate());

                // Create a string for the new user , and populates it with data
                Object newUserId = userContainer.addItem();
                Item row = userContainer.getItem(newUserId);

                row.getItemProperty(Columns.ID).setValue(String.valueOf(id));
                row.getItemProperty(Columns.NAME).setValue(name);
                row.getItemProperty(Columns.AGE).setValue(String.valueOf(age));
                row.getItemProperty(Columns.IS_ADMIN).setValue(String.valueOf(isAdmin));
                row.getItemProperty(Columns.CREATED_DATE).setValue(date);

                userTable.setCurrentPage(getTotalAmountOfPages());
                userTable.select(newUserId);
                userTable.setCurrentPageFirstItemId(newUserId);

                // cleaning fields and close newUserWindow
                cancelButton.click();
            }
        });

        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                windowNameField.setValue("");
                windowAgeField.setValue("");
                windowCheckBox.setValue(false);
                newUserWindow.close();
            }
        });
    }
}

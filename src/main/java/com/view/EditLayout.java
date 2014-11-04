package com.view;

import com.controller.Columns;
import com.model.User;
import com.service.UserService;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EditLayout extends FormLayout{

    @Autowired
    private UserService userService;

    @Autowired
    private UserTable userTable;

    private Button removeUserButton = new Button("Remove");
    private Button saveOldUserButton = new Button("Save");

    private FieldGroup editorFields = new FieldGroup();

    public FieldGroup getEditorFields() {
        return editorFields;
    }

    public void initEditor() {

        TextField editNameField = new TextField(Columns.NAME);
        editNameField.setMaxLength(25);
        editNameField.setInputPrompt("Enter name");

        TextField editAgeField = new TextField(Columns.AGE);
        editAgeField.setMaxLength(3);
        editAgeField.setInputPrompt("Enter age");

        CheckBox editIsAdmin = new CheckBox(Columns.IS_ADMIN);

        addComponent(removeUserButton);
        addComponent(editNameField);
        addComponent(editAgeField);
        addComponent(editIsAdmin);
        addComponent(saveOldUserButton);

        editorFields.bind(editNameField, Columns.NAME);
        editorFields.bind(editAgeField, Columns.AGE);
        editorFields.bind(editIsAdmin, Columns.IS_ADMIN);

        /*
         * Data can be buffered in the user interface. When doing so, commit()
		 * writes the changes to the data source. Here we choose to write the
		 * changes automatically without calling commit().
		 */
        editorFields.setBuffered(false);
    }

    public void initRemoveSaveButtons() {

        saveOldUserButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                // little magic
                Object userId = userTable.getValue();
                Item item = userTable.getItem(userId);
                String str[] = item.toString().trim().split(" ");
                int id = Integer.parseInt(str[0]);

                String name = str[1];
                if (name.equals("")) {
                    Notification.show("Warning!", "Enter name!", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(str[2]);
                } catch (NumberFormatException e) {
                    Notification.show("Warning!", "Enter digits!", Notification.Type.WARNING_MESSAGE);
                    return;
                }

                boolean isAdmin = Boolean.parseBoolean(str[3]);

                User oldUser = userService.getUser(id);
                oldUser.setName(name);
                oldUser.setAge(age);
                oldUser.setAdmin(isAdmin);
                userService.saveOrUpdate(oldUser);

                Notification.show("User saved!", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        removeUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object userId = userTable.getValue();

                /* From Table userTable get item ( includes data from the line)
                 * Using split() gets an array of strings , where the first element
                 * ID of the user you want to delete from the database
                 */
                Item item = userTable.getItem(userId);

                String str[] = item.toString().split(" ");
                int id = Integer.parseInt(str[0]);

                try {
                    userService.deleteUser(userService.getUser(id));
                    Notification.show("Removed", Notification.Type.TRAY_NOTIFICATION);
                } catch (Exception e) {
                    Notification
                            .show("Remove failed: " + e.getCause().getMessage(),
                                    Notification.Type.TRAY_NOTIFICATION);
                }
                userTable.removeItem(userId);
            }
        });
    }
}

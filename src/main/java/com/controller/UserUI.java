package com.controller;

import com.jensjansson.pagedtable.PagedTable;
import com.model.User;
import com.service.UserService;
import com.utils.FillingDB;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@Theme("mytheme")
@SuppressWarnings("serial")
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserUI extends UI {

    @Autowired
    private UserService userService;

    @Autowired
    private FillingDB fillDB;

    private PagedTable userList = new PagedTable();

    private Window window = new Window("New User");

    private TextField windowNameField = new TextField("Name");
    private TextField windowAgeField = new TextField("Age");
    private TextField searchField = new TextField();

    private CheckBox windowCheckBox = new CheckBox("Admin", false);

    private Button addNewUserButton = new Button("New");
    private Button removeUserButton = new Button("Remove");
    private Button saveOldUserButton = new Button("Save");
    private Button saveNewUserButton = new Button("Save");
    private Button cancelButton = new Button("Cancel");

    private FormLayout editorLayout = new FormLayout();
    private FieldGroup editorFields = new FieldGroup();

    private static final String ID = "ID";
    private static final String NAME = "Name";
    private static final String AGE = "Age";
    private static final String IS_ADMIN = "Admin";
    private static final String CREATED_DATE = "Date";

    private static final String[] fieldNames = new String[]{ID, NAME,
            AGE, IS_ADMIN, CREATED_DATE};

    private IndexedContainer userContainer;

    @Override
    protected void init(VaadinRequest request) {
        fillDB.fillDB(); //TODO
        userContainer = readDataSource();
        initLayout();
        initUserList();
        initEditor();
        initSearch();
        initAddRemoveSaveCancelButtons();
    }

    private void initLayout() {

        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        setContent(splitPanel);

		/* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(userList);


        HorizontalLayout paging = initPaging();
        leftLayout.addComponent(paging);

        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewUserButton);
        searchField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        addNewUserButton.addStyleName(ValoTheme.BUTTON_SMALL);

        leftLayout.setComponentAlignment(userList, Alignment.TOP_CENTER);
        leftLayout.setComponentAlignment(paging, Alignment.BOTTOM_CENTER);
        leftLayout.setComponentAlignment(bottomLeftLayout, Alignment.BOTTOM_CENTER);

		/* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

		/*
         * On the left side, expand the size of the userList so that it uses
		 * all the space left after from bottomLeftLayout
		 */
        leftLayout.setExpandRatio(userList, 0.88f);
        leftLayout.setExpandRatio(paging, 0.06f);
        leftLayout.setExpandRatio(bottomLeftLayout, 0.04f);

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

        TextField editNameField = new TextField(NAME);
        editNameField.setMaxLength(25);
        editNameField.setInputPrompt("Enter name");

        TextField editAgeField = new TextField(AGE);
        editAgeField.setMaxLength(3);
        editAgeField.setInputPrompt("Enter age");

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

    private int getTotalAmountOfPages() {
        int size = userContainer.size();
        int pageLength = userList.getPageLength();
        int pageCount;
        if (size % pageLength == 0) {
            pageCount = size / pageLength;
        } else pageCount = size / pageLength + 1;

        return pageCount;
    }

    private HorizontalLayout initPaging() {
        /*
         * Something taken from com.jensjansson.pagedtable.PagedTable from https://vaadin.com/directory#addon/pagedtable
         */
        HorizontalLayout pagingRow = new HorizontalLayout();

        final TextField pageCount = new TextField();
        pageCount.setWidth(100.0f, Sizeable.Unit.PIXELS);
        pageCount.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        pageCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        pageCount.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        pageCount.setInputPrompt(userList.getCurrentPage() + " / " + getTotalAmountOfPages());

        pageCount.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
                if (pageCount.isValid() && pageCount.getValue() != null) {
                    String page = pageCount.getValue();
                    int pageNumber;
                    try {
                        pageNumber = Integer.valueOf(page);
                    } catch (NumberFormatException e) {
                        if (!page.equals(""))
                            Notification.show("Warning!", "Enter digits!", Notification.Type.WARNING_MESSAGE);
                        pageCount.setValue("");
                        userList.focus();
                        return;
                    }
                    userList.setCurrentPage(pageNumber);
                    pageCount.setValue("");
                    userList.focus();
                }
            }
        });

        Button first = new Button("|«", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userList.setCurrentPage(0);
            }
        });
        first.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        first.addStyleName(ValoTheme.BUTTON_SMALL);

        Button last = new Button("»|", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userList.setCurrentPage(getTotalAmountOfPages());
            }
        });
        last.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        last.addStyleName(ValoTheme.BUTTON_SMALL);

        Button previous = new Button("«", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userList.previousPage();
            }
        });
        previous.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        previous.addStyleName(ValoTheme.BUTTON_SMALL);

        Button next = new Button("»", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userList.nextPage();
            }
        });
        next.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        next.addStyleName(ValoTheme.BUTTON_SMALL);

        pagingRow.addComponent(first);
        pagingRow.addComponent(previous);
        pagingRow.addComponent(pageCount);
        pagingRow.addComponent(next);
        pagingRow.addComponent(last);

        pagingRow.setComponentAlignment(pageCount, Alignment.MIDDLE_CENTER);

        userList.addListener(new PagedTable.PageChangeListener() {
            @Override
            public void pageChanged(PagedTable.PagedTableChangeEvent pagedTableChangeEvent) {
                pageCount.setInputPrompt(userList.getCurrentPage() + " / " + getTotalAmountOfPages());
            }
        });

        return pagingRow;
    }

    @SuppressWarnings("unchecked")
    private void initAddRemoveSaveCancelButtons() {

        addNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                HorizontalLayout windowLayout = new HorizontalLayout();
                windowLayout.setSizeFull();
                windowLayout.addStyleName("outlined");

                window.setContent(windowLayout);
                window.setWidth(300.0f, Unit.PIXELS); // Window size
                window.center();  // Sets this window to be centered
                window.setModal(true); // Sets window modality
                window.setResizable(false);  // change the size
                window.setDraggable(false);  // window can be dragged

                FormLayout content = new FormLayout();
                window.setContent(content);
                UI.getCurrent().addWindow(window);
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
            }
        });

        saveOldUserButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                // little magic
                Object userId = userList.getValue();
                Item item = userList.getItem(userId);
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

        saveNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                String name = windowNameField.getValue().trim();
                if (name.equals("")) {
                    Notification.show("Warning!", "Enter name!", Notification.Type.WARNING_MESSAGE);
                    window.close();
                    return;
                }

                int age;
                try {
                    age = Integer.parseInt(windowAgeField.getValue());
                } catch (NumberFormatException e) {
                    Notification.show("Warning!", "Enter digits!", Notification.Type.WARNING_MESSAGE);
                    window.close();
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

                row.getItemProperty(ID).setValue(String.valueOf(id));
                row.getItemProperty(NAME).setValue(name);
                row.getItemProperty(AGE).setValue(String.valueOf(age));
                row.getItemProperty(IS_ADMIN).setValue(String.valueOf(isAdmin));
                row.getItemProperty(CREATED_DATE).setValue(date);

                userList.setCurrentPage(getTotalAmountOfPages());
                userList.select(newUserId);
                userList.setCurrentPageFirstItemId(newUserId);

                // cleaning fields and close window
                cancelButton.click();
            }
        });

        removeUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                Object userId = userList.getValue();

                /* From Table userList get item ( includes data from the line)
                 * Using split() gets an array of strings , where the first element
                 * ID of the user you want to delete from the database
                 */
                Item item = userList.getItem(userId);

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

    private void initUserList() {
        userList.setContainerDataSource(userContainer);
        userList.setVisibleColumns(fieldNames);
        userList.setSelectable(true);
        userList.setWidth("100%");
        userList.setImmediate(true);
        userList.setRowHeaderMode(Table.RowHeaderMode.INDEX);
        userList.setPageLength(24);                             // Page Length
        userList.addStyleName(ValoTheme.TABLE_COMPACT);

        userList.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Object userId = userList.getValue();

				/*
                 * When a user is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our user at once.
				 */
                if (userId != null) {
                    editorFields.setItemDataSource(userList.getItem(userId));
                }
                editorLayout.setVisible(userId != null);
            }
        });
    }

    private void initSearch() {

        searchField.setInputPrompt("Search users");

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
                userContainer.addContainerFilter(new UserFilter(event.getText()));
                userList.refreshRowCache();
                userList.setCurrentPage(0);
            }
        });
    }

    private class UserFilter implements Container.Filter {
        private String needle;

        public UserFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        public boolean passesFilter(Object itemId, Item item) {
            String haystack = ("" + item.getItemProperty(ID).getValue()
                    + item.getItemProperty(NAME).getValue()
                    + item.getItemProperty(AGE).getValue()).toLowerCase()
                    + item.getItemProperty(IS_ADMIN).getValue()
                    + item.getItemProperty(CREATED_DATE).getValue();
            return haystack.contains(needle);
        }

        public boolean appliesToProperty(Object id) {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    private IndexedContainer readDataSource() {
        IndexedContainer ic = new IndexedContainer();

        for (String p : fieldNames) {
            ic.addContainerProperty(p, String.class, "");
        }

        List<User> users = userService.getAll();
        for (User user : users) {
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

package com.view;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Component
public class UserTableLayout extends HorizontalSplitPanel{

    @Autowired
    private UserTable userTable;

    @Autowired
    private NewUserWindow newUserWindow;

    @Autowired
    private EditLayout editLayout;

    @Autowired
    private PagingPanel pagingPanel;

    @Autowired
    private SearchField searchField;

    private Button addNewUserButton = new Button("New");

    public void initLayout() {

		/* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        addComponent(leftLayout);
        addComponent(editLayout);

        leftLayout.addComponent(userTable);
        leftLayout.addComponent(pagingPanel);

        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(searchField);
        bottomLeftLayout.addComponent(addNewUserButton);

        searchField.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        addNewUserButton.addStyleName(ValoTheme.BUTTON_SMALL);

        leftLayout.setComponentAlignment(userTable, Alignment.TOP_CENTER);
        leftLayout.setComponentAlignment(pagingPanel, Alignment.BOTTOM_CENTER);
        leftLayout.setComponentAlignment(bottomLeftLayout, Alignment.BOTTOM_CENTER);

		/* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();

		/*
         * On the left side, expand the size of the userTable so that it uses
		 * all the space left after from bottomLeftLayout
		 */
        leftLayout.setExpandRatio(userTable, 0.88f);
        leftLayout.setExpandRatio(pagingPanel, 0.06f);
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
        editLayout.setMargin(true);
        editLayout.setVisible(false);

        addNewUserButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {

                newUserWindow.getNewUserWindow();
            }
        });
    }
}

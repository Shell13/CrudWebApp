package com.view;

import com.controller.Columns;
import com.jensjansson.pagedtable.PagedTable;
import com.controller.UserContainer;
import com.logger.LoggerWrapper;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UserTable extends PagedTable {

    private static final LoggerWrapper LOG = LoggerWrapper.get(UserTable.class);

    @Autowired
    private UserContainer userContainer;

    @Autowired
    private EditLayout editLayout;

    public void initUserTable() {
        LOG.info("initUserTable");

        setContainerDataSource(userContainer);

        setVisibleColumns(Columns.COLUMNS);
        setSelectable(true);
        setWidth("100%");
        setImmediate(true);
        setRowHeaderMode(Table.RowHeaderMode.INDEX);
        setPageLength(24);                             // Page Length
        addStyleName(ValoTheme.TABLE_COMPACT);

        addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {

                Object userId = getValue();

				/*
                 * When a user is selected from the list, we want to show
				 * that in our editor on the right. This is nicely done by the
				 * FieldGroup that binds all the fields to the corresponding
				 * Properties in our user at once.
				 */
                if (userId != null) {
                    editLayout.getEditorFields().setItemDataSource(getItem(userId));
                }
                editLayout.setVisible(userId != null);
            }
        });
    }
}

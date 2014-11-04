package com.view;

import com.controller.Columns;
import com.controller.UserContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SearchField extends TextField{

    @Autowired
    private UserContainer userContainer;

    @Autowired
    private UserTable userTable;

    public void initSearch() {

        setInputPrompt("Search users");

		/*
         * When the event happens, we handle it in the anonymous inner class.
		 * You may choose to use separate controllers (in MVC) or presenters (in
		 * MVP) instead. In the end, the preferred application architecture is
		 * up to you.
		 */
        addTextChangeListener(new FieldEvents.TextChangeListener() {
            public void textChange(final FieldEvents.TextChangeEvent event) {

				/* Reset the filter for the userContainer. */
                userContainer.removeAllContainerFilters();
                userContainer.addContainerFilter(new UserFilter(event.getText()));
                userTable.refreshRowCache();
                userTable.setCurrentPage(0);
            }
        });
    }

    private class UserFilter implements Container.Filter {
        private String needle;

        public UserFilter(String needle) {
            this.needle = needle.toLowerCase();
        }

        public boolean passesFilter(Object itemId, Item item) {
            String haystack = ("" + item.getItemProperty(Columns.ID).getValue()
                    + item.getItemProperty(Columns.NAME).getValue()
                    + item.getItemProperty(Columns.AGE).getValue()).toLowerCase()
                    + item.getItemProperty(Columns.IS_ADMIN).getValue()
                    + item.getItemProperty(Columns.CREATED_DATE).getValue();
            return haystack.contains(needle);
        }

        public boolean appliesToProperty(Object id) {
            return true;
        }
    }
}

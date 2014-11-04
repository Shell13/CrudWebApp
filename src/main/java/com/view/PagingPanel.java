package com.view;

import com.jensjansson.pagedtable.PagedTable;
import com.controller.UserContainer;
import com.vaadin.data.Property;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Component
public class PagingPanel extends HorizontalLayout{

    @Autowired
    private UserTable userTable;

    @Autowired
    private UserContainer userContainer;

    public void initPaging() {
        /*
         * Something taken from com.jensjansson.pagedtable.PagedTable from https://vaadin.com/directory#addon/pagedtable
         */

        final TextField pageCount = new TextField();
        pageCount.setWidth(100.0f, Sizeable.Unit.PIXELS);
        pageCount.addStyleName(ValoTheme.TEXTFIELD_ALIGN_CENTER);
        pageCount.addStyleName(ValoTheme.TEXTFIELD_BORDERLESS);
        pageCount.addStyleName(ValoTheme.TEXTFIELD_SMALL);

        pageCount.setInputPrompt(userTable.getCurrentPage() + " / " + getTotalAmountOfPages());

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
                        userTable.focus();
                        return;
                    }
                    userTable.setCurrentPage(pageNumber);
                    pageCount.setValue("");
                    userTable.focus();
                }
            }
        });

        Button first = new Button("|«", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userTable.setCurrentPage(0);
            }
        });
        first.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        first.addStyleName(ValoTheme.BUTTON_SMALL);

        Button last = new Button("»|", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userTable.setCurrentPage(getTotalAmountOfPages());
            }
        });
        last.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        last.addStyleName(ValoTheme.BUTTON_SMALL);

        Button previous = new Button("«", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userTable.previousPage();
            }
        });
        previous.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        previous.addStyleName(ValoTheme.BUTTON_SMALL);

        Button next = new Button("»", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                userTable.nextPage();
            }
        });
        next.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        next.addStyleName(ValoTheme.BUTTON_SMALL);

        addComponent(first);
        addComponent(previous);
        addComponent(pageCount);
        addComponent(next);
        addComponent(last);

        setComponentAlignment(pageCount, Alignment.MIDDLE_CENTER);

        userTable.addListener(new PagedTable.PageChangeListener() {
            @Override
            public void pageChanged(PagedTable.PagedTableChangeEvent pagedTableChangeEvent) {
                pageCount.setInputPrompt(userTable.getCurrentPage() + " / " + getTotalAmountOfPages());
            }
        });
    }

    private int getTotalAmountOfPages() {
        int size = userContainer.size();
        int pageLength = userTable.getPageLength();
        int pageCount;
        if (size % pageLength == 0) {
            pageCount = size / pageLength;
        } else pageCount = size / pageLength + 1;

        return pageCount;
    }
}

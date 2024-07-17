/*
 * Copyright 2007-2024, Niclas Hedhman, Sweden
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.library.web.vaadin.crud.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.HashMap;
import java.util.Map;
import org.qi4j.library.web.vaadin.crud.CrudOperation;
import org.qi4j.library.web.vaadin.crud.layout.CrudLayout;

public class WindowBasedCrudLayout extends Composite<VerticalLayout> implements CrudLayout, HasSize
{
    protected VerticalLayout mainLayout = new VerticalLayout();
    protected HorizontalLayout headerLayout = new HorizontalLayout();
    protected HorizontalLayout toolbarLayout = new HorizontalLayout();
    protected HorizontalLayout filterLayout = new HorizontalLayout();
    protected VerticalLayout mainComponentLayout = new VerticalLayout();
    protected Dialog dialog;
    protected String formWindowWidth;

    protected Map<CrudOperation, String> windowCaptions = new HashMap<>();

    public WindowBasedCrudLayout()
    {
        getContent().setPadding(false);
        getContent().setMargin(false);
        getContent().add(mainLayout);

        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        setSizeFull();

        headerLayout.setVisible(false);
        headerLayout.setSpacing(true);
        headerLayout.setMargin(true);

        toolbarLayout.setVisible(false);
        // FIXME find out Lumo style equivalent
        // toolbarLayout.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        headerLayout.add(toolbarLayout);

        filterLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        filterLayout.setVisible(false);
        filterLayout.setSpacing(true);
        headerLayout.add(filterLayout);

        Icon icon = VaadinIcon.SEARCH.create();
        icon.setSize(".9em");
        filterLayout.add(icon);

        mainComponentLayout.setWidth("100%");
        mainComponentLayout.setHeight(null);
        mainComponentLayout.setMargin(false);
        mainComponentLayout.setPadding(false);
        mainComponentLayout.setId("mainComponentLayout");
        mainLayout.add(mainComponentLayout);
        mainLayout.expand(mainComponentLayout);

        setWindowCaption(CrudOperation.ADD, "Add");
        setWindowCaption(CrudOperation.UPDATE, "Update");
        setWindowCaption(CrudOperation.DELETE, "Are you sure you want to delete this item?");
    }

    @Override
    public void setMainComponent(Component component)
    {
        mainComponentLayout.removeAll();
        mainComponentLayout.add(component);
    }

    @Override
    public void addFilterComponent(Component component)
    {
        if (!headerLayout.isVisible())
        {
            headerLayout.setVisible(true);
            mainLayout.getElement().insertChild(mainLayout.getComponentCount() - 1, headerLayout.getElement());
        }

        filterLayout.setVisible(true);
        filterLayout.add(component);
    }

    @Override
    public void addToolbarComponent(Component component)
    {
        if (!headerLayout.isVisible())
        {
            headerLayout.setVisible(true);
            mainLayout.getElement().insertChild(mainLayout.getComponentCount() - 1, headerLayout.getElement());
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.add(component);
    }

    @Override
    public void showDialog(String caption, Component form)
    {
        VerticalLayout dialogLayout = new VerticalLayout(form);
        dialogLayout.setWidth("100%");
        dialogLayout.setMargin(false);
        dialogLayout.setPadding(false);

        dialog = new Dialog(new H3(caption), dialogLayout);
        dialog.setWidth(formWindowWidth);
        dialog.open();
    }

    @Override
    public void showForm(CrudOperation operation, Component form, String formCaption)
    {
        if (!operation.equals(CrudOperation.READ))
        {
            String caption = (formCaption != null ? formCaption : windowCaptions.get(operation));
            showDialog(caption, form);
        }
    }

    @Override
    public void hideForm()
    {
        if (dialog != null)
        {
            dialog.close();
        }
    }

    public void setWindowCaption(CrudOperation operation, String caption)
    {
        windowCaptions.put(operation, caption);
    }

    public void setFormWindowWidth(String formWindowWidth)
    {
        this.formWindowWidth = formWindowWidth;
    }
}

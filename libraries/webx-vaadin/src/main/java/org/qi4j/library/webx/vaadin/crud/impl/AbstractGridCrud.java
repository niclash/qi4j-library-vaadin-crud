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
package org.qi4j.library.webx.vaadin.crud.impl;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import org.apache.polygene.api.object.ObjectFactory;
import org.qi4j.library.webx.vaadin.crud.CrudOperation;
import org.qi4j.library.webx.vaadin.crud.AbstractCrud;
import org.qi4j.library.webx.vaadin.crud.CrudListener;
import org.qi4j.library.webx.vaadin.crud.CrudOperationException;
import org.qi4j.library.webx.vaadin.crud.form.CrudFormFactory;
import org.qi4j.library.webx.vaadin.crud.form.impl.form.factory.DefaultCrudFormFactory;
import org.qi4j.library.webx.vaadin.crud.layout.CrudLayout;
import org.qi4j.library.webx.vaadin.crud.layout.impl.WindowBasedCrudLayout;

public abstract class AbstractGridCrud<T> extends AbstractCrud<T>
{
    protected String rowCountCaption = "%d items(s) found";
    protected String savedMessage = "Item saved";
    protected String deletedMessage = "Item deleted";
    protected boolean showNotifications = true;

    protected Button findAllButton;
    protected Button addButton;
    protected Button updateButton;
    protected Button deleteButton;
    protected Grid<T> grid;

    private boolean clickRowToUpdate;

    AbstractGridCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory,
                     CrudListener<T> crudListener, ObjectFactory obf)
    {
        super(
            domainType,
            crudLayout != null ? crudLayout : obf.newObject(WindowBasedCrudLayout.class),
            crudFormFactory != null ? crudFormFactory : obf.newObject(DefaultCrudFormFactory.class, domainType),
            crudListener);
        initLayout();
    }

    protected void initLayout()
    {
        findAllButton = new Button(VaadinIcon.REFRESH.create(), e -> findAllButtonClicked());
        findAllButton.getElement().setAttribute("title", "Refresh list");

        crudLayout.addToolbarComponent(findAllButton);

        addButton = new Button(VaadinIcon.PLUS.create(), e -> addButtonClicked());
        addButton.getElement().setAttribute("title", "Add");
        crudLayout.addToolbarComponent(addButton);

        updateButton = new Button(VaadinIcon.PENCIL.create(), e -> updateButtonClicked());
        updateButton.getElement().setAttribute("title", "Update");
        crudLayout.addToolbarComponent(updateButton);

        deleteButton = new Button(VaadinIcon.TRASH.create(), e -> deleteButtonClicked());
        deleteButton.getElement().setAttribute("title", "Delete");
        crudLayout.addToolbarComponent(deleteButton);

        grid = createGrid();
        grid.addSelectionListener(e -> gridSelectionChanged());
        crudLayout.setMainComponent(grid);

        updateButtons();
    }

    protected abstract Grid<T> createGrid();

    public abstract void refreshGrid();

    @Override
    protected void onAttach(AttachEvent attachEvent)
    {
        super.onAttach(attachEvent);
        refreshGrid();
    }

    @Override
    public void setAddOperationVisible(boolean visible)
    {
        addButton.setVisible(visible);
    }

    @Override
    public void setUpdateOperationVisible(boolean visible)
    {
        updateButton.setVisible(visible);
    }

    @Override
    public void setDeleteOperationVisible(boolean visible)
    {
        deleteButton.setVisible(visible);
    }

    @Override
    public void setFindAllOperationVisible(boolean visible)
    {
        findAllButton.setVisible(visible);
    }

    public void setClickRowToUpdate(boolean clickRowToUpdate)
    {
        this.clickRowToUpdate = clickRowToUpdate;
    }

    protected void updateButtons()
    {
        boolean rowSelected = !grid.asSingleSelect().isEmpty();
        updateButton.setEnabled(rowSelected);
        deleteButton.setEnabled(rowSelected);
    }

    protected void gridSelectionChanged()
    {
        updateButtons();
        T domainObject = grid.asSingleSelect().getValue();

        if (domainObject != null)
        {
            if (clickRowToUpdate)
            {
                updateButtonClicked();
            }
            else
            {
                Component form = crudFormFactory.buildNewForm(CrudOperation.READ, domainObject, true, null,
                    event -> grid.asSingleSelect().clear());
                String caption = crudFormFactory.buildCaption(CrudOperation.READ, domainObject);
                crudLayout.showForm(CrudOperation.READ, form, caption);
            }
        }
        else
        {
            crudLayout.hideForm();
        }
    }

    protected void findAllButtonClicked()
    {
        grid.asSingleSelect().clear();
        refreshGrid();

        int count = countRows();
        showNotification(String.format(rowCountCaption, count));
    }

    private int countRows()
    {
        var query = new Query();
        var provider = grid.getDataProvider();
        if (HierarchicalDataProvider.class.isAssignableFrom(provider.getClass()))
        {query = new HierarchicalQuery(null, null);}

        return provider.size(query);
    }

    protected void addButtonClicked()
    {
        T domainObject = crudFormFactory.getNewInstanceSupplier().get();
        showForm(CrudOperation.ADD, domainObject, false, savedMessage, event -> {
            try
            {
                T addedObject = addOperation.perform(domainObject);
                refreshGrid();
                grid.asSingleSelect().setValue(addedObject);
                grid.deselect(addedObject);
                showNotification(savedMessage);
                grid.scrollToItem(addedObject);
            }
            catch (IllegalArgumentException ignore)
            {
            }
        });
    }

    protected void updateButtonClicked()
    {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.UPDATE, domainObject, false, savedMessage, event -> {
            try
            {
                T updatedObject = updateOperation.perform(domainObject);
                grid.asSingleSelect().clear();
                refreshGrid();
                grid.asSingleSelect().setValue(updatedObject);
                grid.deselect(updatedObject);
                showNotification(savedMessage);
                grid.scrollToItem(updatedObject);
            }
            catch (CrudOperationException e1)
            {
                showNotification(e1.getMessage());
                throw e1;
            }
        });
    }

    protected void deleteButtonClicked()
    {
        T domainObject = grid.asSingleSelect().getValue();
        showForm(CrudOperation.DELETE, domainObject, true, deletedMessage, event -> {
            try
            {
                deleteOperation.perform(domainObject);
                refreshGrid();
                grid.asSingleSelect().clear();
                showNotification(deletedMessage);
            }
            catch (CrudOperationException e1)
            {
                showNotification(e1.getMessage());
                refreshGrid();
            }
            catch (Exception e2)
            {
                refreshGrid();
                throw e2;
            }
        });
    }

    protected void showForm(CrudOperation operation, T domainObject, boolean readOnly, String successMessage,
                            ComponentEventListener<ClickEvent<Button>> buttonClickListener)
    {
        Component form = crudFormFactory.buildNewForm(operation, domainObject, readOnly, cancelClickEvent -> {
            if (clickRowToUpdate)
            {
                grid.asSingleSelect().clear();
            }
            else
            {
                T selected = grid.asSingleSelect().getValue();
                crudLayout.hideForm();
                grid.asSingleSelect().clear();
                grid.asSingleSelect().setValue(selected);
            }
        }, operationPerformedClickEvent -> {
            buttonClickListener.onComponentEvent(operationPerformedClickEvent);
            if (!clickRowToUpdate)
            {
                crudLayout.hideForm();
            }
        });
        String caption = crudFormFactory.buildCaption(operation, domainObject);
        crudLayout.showForm(operation, form, caption);
    }

    public Grid<T> getGrid()
    {
        return grid;
    }

    public Button getFindAllButton()
    {
        return findAllButton;
    }

    public Button getAddButton()
    {
        return addButton;
    }

    public Button getUpdateButton()
    {
        return updateButton;
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }

    public void setRowCountCaption(String rowCountCaption)
    {
        this.rowCountCaption = rowCountCaption;
    }

    public void setSavedMessage(String savedMessage)
    {
        this.savedMessage = savedMessage;
    }

    public void setDeletedMessage(String deletedMessage)
    {
        this.deletedMessage = deletedMessage;
    }

    public void setShowNotifications(boolean showNotifications)
    {
        this.showNotifications = showNotifications;
    }

    public void showNotification(String text)
    {
        if (showNotifications)
        {
            Notification.show(text);
        }
    }

}

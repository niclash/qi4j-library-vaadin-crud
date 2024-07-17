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
package org.qi4j.library.webx.vaadin.crud;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import java.util.Collections;
import org.qi4j.library.webx.vaadin.crud.form.CrudFormFactory;
import org.qi4j.library.webx.vaadin.crud.layout.CrudLayout;

public abstract class AbstractCrud<T> extends Composite<VerticalLayout> implements Crud<T>, HasSize
{

    protected Class<T> domainType;

    protected FindAllCrudOperationListener<T> findAllOperation = Collections::emptyList;
    protected AddOperationListener<T> addOperation = t -> null;
    protected UpdateOperationListener<T> updateOperation = t -> null;
    protected DeleteOperationListener<T> deleteOperation = t -> {
    };

    protected CrudLayout crudLayout;
    protected CrudFormFactory<T> crudFormFactory;

    public AbstractCrud(Class<T> domainType, CrudLayout crudLayout, CrudFormFactory<T> crudFormFactory,
                        CrudListener<T> crudListener)
    {
        this.domainType = domainType;
        this.crudLayout = crudLayout;
        this.crudFormFactory = crudFormFactory;

        if (crudListener != null)
        {
            setCrudListener(crudListener);
        }
        getContent().add((Component) crudLayout);

        getContent().setPadding(false);
        getContent().setMargin(false);
        setSizeFull();
    }

    public CrudLayout getCrudLayout()
    {
        return crudLayout;
    }

    @Override
    public void setCrudFormFactory(CrudFormFactory<T> crudFormFactory)
    {
        this.crudFormFactory = crudFormFactory;
    }

    @Override
    public void setFindAllOperation(FindAllCrudOperationListener<T> findAllOperation)
    {
        this.findAllOperation = findAllOperation;
    }

    @Override
    public void setFindAllOperation(DataProvider<T, ?> dataProvider)
    {
        this.findAllOperation = (LazyFindAllCrudOperationListener<T>) () -> dataProvider;
    }

    @Override
    public void setAddOperation(AddOperationListener<T> addOperation)
    {
        this.addOperation = addOperation;
    }

    @Override
    public void setUpdateOperation(UpdateOperationListener<T> updateOperation)
    {
        this.updateOperation = updateOperation;
    }

    @Override
    public void setDeleteOperation(DeleteOperationListener<T> deleteOperation)
    {
        this.deleteOperation = deleteOperation;
    }

    @Override
    public void setOperations(FindAllCrudOperationListener<T> findAllOperation, AddOperationListener<T> addOperation,
                              UpdateOperationListener<T> updateOperation, DeleteOperationListener<T> deleteOperation)
    {
        setFindAllOperation(findAllOperation);
        setAddOperation(addOperation);
        setUpdateOperation(updateOperation);
        setDeleteOperation(deleteOperation);
    }

    @Override
    public CrudFormFactory<T> getCrudFormFactory()
    {
        return crudFormFactory;
    }

    @Override
    public void setCrudListener(CrudListener<T> crudListener)
    {
        setAddOperation(crudListener::add);
        setUpdateOperation(crudListener::update);
        setDeleteOperation(crudListener::delete);

        if (LazyCrudListener.class.isAssignableFrom(crudListener.getClass()))
        {
            setFindAllOperation(
                (LazyFindAllCrudOperationListener<T>) ((LazyCrudListener) crudListener)::getDataProvider);
        }
        else
        {
            setFindAllOperation(crudListener::findAll);
        }
    }

}
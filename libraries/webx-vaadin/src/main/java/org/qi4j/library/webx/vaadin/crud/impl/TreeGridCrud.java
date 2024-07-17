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

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalDataProvider;
import com.vaadin.flow.function.ValueProvider;
import java.util.Collection;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.qi4j.library.webx.vaadin.crud.CrudListener;
import org.qi4j.library.webx.vaadin.crud.LazyFindAllCrudOperationListener;
import org.qi4j.library.webx.vaadin.crud.form.CrudFormFactory;
import org.qi4j.library.webx.vaadin.crud.layout.CrudLayout;

public class TreeGridCrud<T> extends AbstractGridCrud<T>
{
    private final EntityDescriptor domainDescriptor;
    private ValueProvider<T, Collection<T>> childItemProvider;

    public TreeGridCrud(@Uses Class<T> domainType,
                        @Uses @Optional CrudLayout crudLayout,
                        @Uses @Optional CrudFormFactory<T> crudFormFactory,
                        @Uses @Optional CrudListener<T> crudListener,
                        @Structure Module module,
                        @Structure ObjectFactory obf)
    {
        super(domainType, crudLayout, crudFormFactory, crudListener, obf);
        domainDescriptor = module.typeLookup().lookupEntityModel(domainType);
    }

    @Override
    protected Grid<T> createGrid()
    {
        return new TreeGrid<>(domainType);
    }

    @Override
    public TreeGrid<T> getGrid()
    {
        return (TreeGrid<T>) super.getGrid();
    }

    public void refreshGrid()
    {
        if (LazyFindAllCrudOperationListener.class.isAssignableFrom(findAllOperation.getClass()))
        {
            LazyFindAllCrudOperationListener findAll = (LazyFindAllCrudOperationListener) findAllOperation;
            DataProvider dataProvider = findAll.getDataProvider();

            if (!HierarchicalDataProvider.class.isAssignableFrom(dataProvider.getClass()))
            {
                throw new UnsupportedOperationException(
                    "The data provider for TreeGridCrud must implement HierarchicalDataProvider");
            }

            getGrid().setItems(dataProvider);
        }
        else
        {
            Collection<T> items = findAllOperation.findAll();
            getGrid().setItems(items, childItemProvider);
        }
    }

    public ValueProvider<T, Collection<T>> getChildItemProvider()
    {
        return childItemProvider;
    }

    public void setChildItemProvider(ValueProvider<T, Collection<T>> childItemProvider)
    {
        this.childItemProvider = childItemProvider;
    }
}

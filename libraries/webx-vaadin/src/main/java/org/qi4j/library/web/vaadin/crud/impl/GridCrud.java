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
package org.qi4j.library.web.vaadin.crud.impl;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Stream;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.query.grammar.OrderBy;
import org.apache.polygene.api.query.grammar.PropertyFunction;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.qi4j.library.web.vaadin.crud.CrudListener;
import org.qi4j.library.web.vaadin.crud.LazyFindAllCrudOperationListener;
import org.qi4j.library.web.vaadin.crud.form.CrudFormFactory;
import org.qi4j.library.web.vaadin.crud.layout.CrudLayout;

import static org.apache.polygene.api.query.QueryExpressions.property;
import static org.apache.polygene.api.query.grammar.OrderBy.Order.ASCENDING;
import static org.apache.polygene.api.query.grammar.OrderBy.Order.DESCENDING;

public class GridCrud<T> extends AbstractGridCrud<T>
{
    @Structure
    QueryBuilderFactory qbf;

    @Structure
    UnitOfWorkFactory uowf;

    private Column<T> componentColumn;

    private EntityDescriptor domainDescriptor;

    private final Map<T, Button> map = new WeakHashMap<>();

    public GridCrud(@Uses Class<T> domainType,
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
        BackEndDataProvider<T,Void> dataProvider = new AbstractBackEndDataProvider<>(){

            @Override
            protected Stream<T> fetchFromBackEnd(Query query)
            {
                UnitOfWork uow = uowf.currentUnitOfWork();
                QueryBuilder<T> qb = qbf.newQueryBuilder(domainType);
                org.apache.polygene.api.query.Query<T> qi4jQuery =
                    uow.newQuery(qb);
                qi4jQuery = qi4jQuery
                    .firstResult(query.getPage()/query.getPageSize())
                        .maxResults(query.getLimit());
                List<QuerySortOrder> sortOrders = query.getSortOrders();
                OrderBy[] orderBy = new OrderBy[sortOrders.size()];
                int index = 0;
                for(QuerySortOrder order : sortOrders)
                {
                    String sorted = order.getSorted();
                    PropertyFunction<Object> property = property(property(domainType, sorted));
                    orderBy[index++] = new OrderBy(property, order.getDirection() == SortDirection.ASCENDING ? ASCENDING : DESCENDING );
                }
                qi4jQuery = qi4jQuery.orderBy(orderBy);
                return qi4jQuery.stream();
            }

            @Override
            protected int sizeInBackEnd(Query query)
            {
                UnitOfWork uow = uowf.currentUnitOfWork();
                QueryBuilder<T> qb = qbf.newQueryBuilder(domainType);
                org.apache.polygene.api.query.Query<T> qi4jQuery =
                    uow.newQuery(qb);
                return (int) qi4jQuery.count();
            }
        };
        return new Grid<T>(dataProvider);
    }

    public void refreshGrid()
    {
        if (LazyFindAllCrudOperationListener.class.isAssignableFrom(findAllOperation.getClass()))
        {
            LazyFindAllCrudOperationListener findAll = (LazyFindAllCrudOperationListener) findAllOperation;
            grid.setDataProvider(findAll.getDataProvider());
        }
        else
        {
            Collection<T> items = findAllOperation.findAll();
            grid.setItems(items);
        }
    }

    public void addUpdateButtonColumn()
    {
        if (componentColumn == null)
        {
            componentColumn = grid.addComponentColumn(item -> {
                Button button = new Button(VaadinIcon.PENCIL.create());
                button.addClickListener(e -> {
                    grid.select(item);
                    updateButtonClicked();
                });

                if (map.put(item, button) == null)
                {
                    button.setEnabled(false);
                }

                return button;
            });
        }
    }

    public Button getUpdateButton(T item)
    {
        return map.get(item);
    }

    public void setUpdateButtonColumnEnabled(boolean enabled)
    {
        map.values().stream().forEach(b -> b.setEnabled(enabled));
    }
}

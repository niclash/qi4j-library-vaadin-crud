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
package org.hedhman.niclas.demo.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.object.ObjectFactory;
import org.hedhman.niclas.demo.entity.User;
import org.hedhman.niclas.demo.service.GroupService;
import org.hedhman.niclas.demo.service.UserService;
import org.qi4j.library.web.vaadin.crud.LazyCrudListener;
import org.qi4j.library.web.vaadin.crud.impl.GridCrud;
import org.qi4j.library.web.vaadin.Qi4jManaged;
import org.hedhman.niclas.demo.ui.MainLayout;

@Route(value = "lazy-loading", layout = MainLayout.class)
@Qi4jManaged
public class CrudWithLazyLoadingView extends VerticalLayout
{

    public CrudWithLazyLoadingView(@Service UserService userService, @Service GroupService groupService, @Structure ObjectFactory obf)
    {
        GridCrud<User> crud = obf.newObject(GridCrud.class, User.class);

        Grid<User> grid = crud.getGrid();
        grid.setPageSize(50);
        grid.setColumnReorderingAllowed(true);

        grid.addColumn(user -> user.name().get());
        grid.addColumn(user -> user.birthDate().get());
        grid.addColumn(user -> user.maritalStatus().get());
        grid.addColumn(user -> user.email().get());
        grid.addColumn(user -> user.phoneNumber().get());
        grid.addColumn(user -> user.active().get());
        grid.setColumnReorderingAllowed(true);
        setSizeFull();
        add(crud);
        crud.setCrudListener(new LazyCrudListener<>()
        {
            @Override
            public DataProvider<User, Void> getDataProvider()
            {
				return DataProvider.fromCallbacks(
						query -> userService.findAllWithPaging(query.getPage(), query.getPageSize()).stream(),
						query -> (int) userService.countAll());
            }

            @Override
            public User add(User user)
            {
                return userService.save(user);
            }

            @Override
            public User update(User user)
            {
                return userService.save(user);
            }

            @Override
            public void delete(User user)
            {
                userService.delete(user);
            }
        });
    }

}

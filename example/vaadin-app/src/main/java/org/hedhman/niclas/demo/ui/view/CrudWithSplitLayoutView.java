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

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.hedhman.niclas.demo.service.GroupService;
import org.hedhman.niclas.demo.service.UserService;
import org.qi4j.library.webx.vaadin.crud.impl.GridCrud;
import org.qi4j.library.webx.vaadin.Qi4jManaged;
import org.hedhman.niclas.demo.entity.User;
import org.hedhman.niclas.demo.ui.MainLayout;
import org.qi4j.library.webx.vaadin.crud.form.impl.form.factory.DefaultCrudFormFactory;
import org.qi4j.library.webx.vaadin.crud.layout.impl.HorizontalSplitCrudLayout;

@Route(value = "split-layout", layout = MainLayout.class)
@Qi4jManaged
public class CrudWithSplitLayoutView extends VerticalLayout
{
    public CrudWithSplitLayoutView(@Service UserService userService, @Service GroupService groupService, @Structure ObjectFactory obf)
    {
        SplitLayoutCrudFormFactory<User> formFactory = obf.newObject(SplitLayoutCrudFormFactory.class, User.class);
        GridCrud<User> crud = obf.newObject(GridCrud.class, User.class, new HorizontalSplitCrudLayout(), formFactory);
        crud.setClickRowToUpdate(true);
        crud.setUpdateOperationVisible(false);
        Grid<User> grid = crud.getGrid();
        grid.addColumn(user -> user.name().get());
        grid.addColumn(user -> user.birthDate().get());
        grid.addColumn(user -> user.maritalStatus().get());
        grid.addColumn(user -> user.email().get());
        grid.addColumn(user -> user.phoneNumber().get());
        grid.addColumn(user -> user.active().get());
        grid.setColumnReorderingAllowed(true);
        crud.getGrid().setColumnReorderingAllowed(true);
        setSizeFull();
        add(crud);
        crud.setOperations(
            userService::findAll,
            userService::save,
            userService::save,
            userService::delete);
    }

    public static class SplitLayoutCrudFormFactory<T> extends DefaultCrudFormFactory<T>
    {
        public SplitLayoutCrudFormFactory(@Uses Class<T> domainType, @Structure Module module)
        {
            super(domainType, module);
        }

        @Override
        protected void configureForm(FormLayout formLayout, List<HasValueAndElement> fields)
        {
            Component nameField = (Component) fields.getFirst();
            formLayout.setColspan(nameField, 2);
        }
    }
}

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

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.hedhman.niclas.demo.entity.User;
import org.hedhman.niclas.demo.service.GroupService;
import org.hedhman.niclas.demo.service.UserService;
import org.qi4j.library.webx.vaadin.crud.impl.GridCrud;
import org.qi4j.library.webx.vaadin.Qi4jManaged;
import org.hedhman.niclas.demo.ui.MainLayout;

@Route(value = "simple", layout = MainLayout.class)
@Qi4jManaged
public class SimpleCrudView extends VerticalLayout
{
    @Structure
    UnitOfWorkFactory uowf;

    public SimpleCrudView(@Service UserService userService, @Service GroupService groupService, @Structure ObjectFactory obf, @Structure Module module)
    {

        Button saveButton = new Button("Save", ev -> {
            UnitOfWork uow = uowf.currentUnitOfWork();
        });
        add(saveButton);

        // crud instance
        GridCrud<User> crud = obf.newObject(GridCrud.class, User.class);

        // grid configuration
        Grid<User> grid = crud.getGrid();
        grid.addColumn(user -> user.name().get());
        grid.addColumn(user -> user.birthDate().get());
        grid.addColumn(user -> user.maritalStatus().get());
        grid.addColumn(user -> user.email().get());
        grid.addColumn(user -> user.phoneNumber().get());
        grid.addColumn(user -> user.active().get());
        grid.setColumnReorderingAllowed(true);

//        crudFormFactory.setFieldProvider("mainGroup"),
//            new ComboBoxProvider<>(groupService.findAll()));
//        crudFormFactory.setFieldProvider("groups",
//            new CheckBoxGroupProvider<>(groupService.findAll()));
//        crudFormFactory.setFieldProvider("groups",
//            new CheckBoxGroupProvider<>("Groups", groupService.findAll(), group -> group.name().get()));
//        crudFormFactory.setFieldProvider("mainGroup",
//            new ComboBoxProvider<>("Main Group",
//                groupService.findAll(),
//                new TextRenderer<>(group -> group.name().get()),
//                group -> group.name().get()));

        setSizeFull();
        add(crud);
        crud.setFindAllOperationVisible(false);
        crud.setOperations(
            userService::findAll,
            userService::save,
            userService::save,
            userService::delete);
    }
}

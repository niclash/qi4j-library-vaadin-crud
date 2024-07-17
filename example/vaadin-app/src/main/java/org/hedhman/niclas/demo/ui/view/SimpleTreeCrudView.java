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

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.apache.polygene.api.structure.Module;
import org.hedhman.niclas.demo.entity.Technology;
import org.hedhman.niclas.demo.service.TechnologyService;
import org.qi4j.library.web.vaadin.crud.impl.TreeGridCrud;
import org.qi4j.library.web.vaadin.Qi4jManaged;
import org.hedhman.niclas.demo.ui.MainLayout;
import org.qi4j.library.web.vaadin.crud.form.CrudFormFactory;

@Route(value = "simple-tree", layout = MainLayout.class)
@Qi4jManaged
public class SimpleTreeCrudView extends VerticalLayout
{
    public SimpleTreeCrudView(@Service TechnologyService technologyService, @Structure ObjectFactory obf, @Structure Module module)
    {
        TreeGridCrud<Technology> crud = obf.newObject(TreeGridCrud.class, Technology.class);
        crud.getGrid().removeAllColumns();
        crud.getGrid().addHierarchyColumn(technology1 -> technology1.name().get()).setHeader("Name");
        crud.setChildItemProvider((Technology t) -> t.children().toList());
        CrudFormFactory<Technology> crudFormFactory = crud.getCrudFormFactory();
        Map<String, ? extends PropertyDescriptor> properties = crudFormFactory.discoverProperties().collect(Collectors.toMap((pd) -> pd.qualifiedName().name(), pd -> pd));
        crudFormFactory.setFieldProvider(properties.get("description"), technology -> new TextArea());
        crudFormFactory.setFieldProvider(properties.get("parent"),
            technology -> new ComboBox<>("Parent", technologyService.findAll()));
        crud.setFindAllOperation(technologyService::findRoots);
        crud.setAddOperation(technologyService::save);
        crud.setUpdateOperation(technologyService::save);
        crud.setDeleteOperation(technologyService::delete);
        crud.setChildItemProvider(technologyService::findChildren);
        crud.setShowNotifications(false);
        addAndExpand(crud);
        setSizeFull();
    }

}

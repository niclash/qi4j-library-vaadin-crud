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
package org.hedhman.niclas.demo.bootstrap;

import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;
import org.qi4j.library.web.UnitOfWorkFilter;
import org.qi4j.library.web.vaadin.crud.impl.GridCrud;
import org.qi4j.library.web.vaadin.crud.impl.TreeGridCrud;
import org.qi4j.library.web.vaadin.Qi4jInstantiator;
import org.hedhman.niclas.demo.ui.view.CrudWithFilterView;
import org.hedhman.niclas.demo.ui.view.CrudWithLazyLoadingView;
import org.hedhman.niclas.demo.ui.view.CrudWithSplitLayoutView;
import org.hedhman.niclas.demo.ui.view.HomeView;
import org.hedhman.niclas.demo.ui.view.SimpleCrudView;
import org.hedhman.niclas.demo.ui.view.SimpleTreeCrudView;
import org.qi4j.library.web.vaadin.crud.form.impl.form.factory.DefaultCrudFormFactory;
import org.qi4j.library.web.vaadin.crud.layout.impl.WindowBasedCrudLayout;

public class VaadinModule implements ModuleAssembler
{
    public static String NAME;

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module)
        throws AssemblyException
    {
        module.defaultServices();
        module.objects(
            Qi4jInstantiator.class,
            UnitOfWorkFilter.class,
            GridCrud.class,
            TreeGridCrud.class,
            CrudWithFilterView.class,
            CrudWithLazyLoadingView.class,
            CrudWithSplitLayoutView.class,
            CrudWithSplitLayoutView.SplitLayoutCrudFormFactory.class,
            SimpleCrudView.class,
            SimpleTreeCrudView.class,
            WindowBasedCrudLayout.class,
            DefaultCrudFormFactory.class,
            HomeView.class
        );
        return module;
    }
}

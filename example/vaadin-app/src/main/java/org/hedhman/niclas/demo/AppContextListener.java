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
package org.hedhman.niclas.demo;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.EnumSet;
import org.apache.polygene.api.activation.PassivationException;
import org.apache.polygene.api.composite.TransientBuilderFactory;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.service.ServiceFinder;
import org.apache.polygene.api.service.ServiceReference;
import org.apache.polygene.api.structure.Application;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.structure.TypeLookup;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.hedhman.niclas.demo.bootstrap.AppAssembler;
import org.hedhman.niclas.demo.bootstrap.VaadinModule;
import org.hedhman.niclas.demo.bootstrap.UiLayer;
import org.apache.polygene.bootstrap.layered.LayeredApplicationAssembler;
import org.qi4j.library.webx.UnitOfWorkFilter;

import static org.apache.polygene.api.structure.Application.Mode.development;

public class AppContextListener
    implements ServletContextListener
{

    public static final String QI4J_VAADIN_MODULE = "qi4j.vaadinModule";
    protected Application qi4jApp;
    protected Module module;
    protected ValueBuilderFactory valueBuilderFactory;
    protected UnitOfWorkFactory unitOfWorkFactory;
    protected TransientBuilderFactory transientBuilderFactory;
    protected ObjectFactory objectFactory;
    protected TypeLookup typeLookup;
    protected ServiceFinder serviceFinder;

    public void contextInitialized(ServletContextEvent sce)
    {
        LayeredApplicationAssembler app = createApplicationAssembler();
        try
        {
            app.initialize();
            app.start();
        }
        catch (Throwable e)
        {
            throw new UndeclaredThrowableException(e, "Failed to start Qi4j application.");
        }
        qi4jApp = app.application();
        module = findVaadinModule();
        ServletContext context = sce.getServletContext();
        unitOfWorkFactory = module.unitOfWorkFactory();
        valueBuilderFactory = module.valueBuilderFactory();
        transientBuilderFactory = module.transientBuilderFactory();
        objectFactory = module.objectFactory();
        typeLookup= module.typeLookup();
        serviceFinder = module.serviceFinder();

        Filter filter = objectFactory.newObject(UnitOfWorkFilter.class);
        FilterRegistration.Dynamic filterRegistration = context.addFilter("UnitOfWorkFilter", filter);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST);
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
        context.setAttribute(QI4J_VAADIN_MODULE, module);
        ServiceReference<InitializeDatabase> initData = serviceFinder.findService(InitializeDatabase.class);
        if (initData.isAvailable())
        {
            initData.get().initialize();
        }
    }

    protected Module findVaadinModule()
    {
        return qi4jApp.findModule(UiLayer.NAME, VaadinModule.NAME);
    }

    protected LayeredApplicationAssembler createApplicationAssembler()
    {
        return new AppAssembler("Demo", "1.0", development);
    }

    public void contextDestroyed(ServletContextEvent sce)
    {
        try
        {
            qi4jApp.passivate();
        }
        catch (PassivationException e)
        {
            throw new UndeclaredThrowableException(e);
        }
    }
}

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
import jakarta.servlet.ServletContextEvent;
import org.apache.polygene.api.service.ServiceReference;
import org.apache.polygene.api.structure.Module;
import org.hedhman.niclas.demo.bootstrap.AppAssembler;
import org.hedhman.niclas.demo.bootstrap.VaadinModule;
import org.hedhman.niclas.demo.bootstrap.UiLayer;
import org.apache.polygene.bootstrap.layered.LayeredApplicationAssembler;
import org.qi4j.library.web.Qi4jContextListener;

import static org.apache.polygene.api.structure.Application.Mode.development;

public class AppContextListener extends Qi4jContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        super.contextInitialized(sce);
        ServiceReference<InitializeDatabase> initData = serviceFinder.findService(InitializeDatabase.class);
        if (initData.isAvailable())
        {
            initData.get().initialize();
        }
    }

    @Override
    protected Module findVaadinModule()
    {
        return qi4jApp.findModule(UiLayer.NAME, VaadinModule.NAME);
    }

    @Override
    protected LayeredApplicationAssembler createApplicationAssembler()
    {
        return new AppAssembler("Demo", "1.0", development);
    }
}

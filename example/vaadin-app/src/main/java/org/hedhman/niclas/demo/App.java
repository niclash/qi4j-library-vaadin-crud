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

import com.vaadin.flow.di.Instantiator;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;
import jakarta.servlet.ServletException;
import org.apache.polygene.api.structure.Module;
import org.qi4j.library.webx.vaadin.Qi4jInstantiator;

import static org.qi4j.library.webx.Qi4jContextListener.QI4J_VAADIN_MODULE;

/**
 * The entry point of the Spring Boot application.
 */
public class App extends VaadinServlet
{
    @Override
    protected VaadinServletService createServletService() throws ServletException, ServiceException {
        return this.createServletService(this.createDeploymentConfiguration());
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        VaadinServletService service = new VaadinServletService(this, deploymentConfiguration){
            @Override
            protected Instantiator createInstantiator()
            {
                Module vaadinQi4jModule = (Module) getServletContext().getAttribute(QI4J_VAADIN_MODULE);
                return vaadinQi4jModule.newObject(Qi4jInstantiator.class, this);
            }
        };
        service.init();
        return service;
    }
}

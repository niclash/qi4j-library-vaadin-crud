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

import org.apache.polygene.api.structure.Application;
import org.apache.polygene.bootstrap.ApplicationAssembly;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.layered.LayeredApplicationAssembler;

public class AppAssembler extends LayeredApplicationAssembler
{
    public AppAssembler(String name, String version, Application.Mode mode)
        throws AssemblyException
    {
        super(name, version, mode);
    }

    @Override
    protected void assembleLayers(ApplicationAssembly assembly)
        throws AssemblyException
    {
        LayerAssembly confLayer = createLayer(ConfigurationLayer.class);
        LayerAssembly infraLayer = createLayer(InfrastructureLayer.class);
        LayerAssembly domainLayer = createLayer(DomainLayer.class);
        LayerAssembly uiLayer = createLayer(UiLayer.class);
        uiLayer.uses(domainLayer);
        domainLayer.uses(infraLayer);
        infraLayer.uses(confLayer);
    }
}

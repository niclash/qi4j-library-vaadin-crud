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
import org.apache.polygene.entitystore.memory.assembly.MemoryEntityStoreAssembler;
import org.apache.polygene.library.fileconfig.FileConfigurationAssembler;
import org.apache.polygene.library.rdf.repository.NativeConfiguration;
import org.apache.polygene.library.uowfile.bootstrap.UoWFileAssembler;

import static org.apache.polygene.api.common.Visibility.application;

public class InfraConfigModule implements ModuleAssembler
{
    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module)
        throws AssemblyException
    {
        module.defaultServices();
        module.configurations(NativeConfiguration.class).visibleIn(application);
        new MemoryEntityStoreAssembler().assemble(module);
        new FileConfigurationAssembler().visibleIn(application).assemble(module);
        new UoWFileAssembler().visibleIn(application).assemble(module);
        return module;
    }
}

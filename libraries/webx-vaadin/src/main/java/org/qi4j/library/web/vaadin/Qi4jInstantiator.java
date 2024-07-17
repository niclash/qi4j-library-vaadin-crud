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
package org.qi4j.library.web.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.di.DefaultInstantiator;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.auth.MenuAccessControl;
import java.util.stream.Stream;
import org.apache.polygene.api.common.Optional;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.structure.TypeLookup;

public class Qi4jInstantiator extends DefaultInstantiator
{
    @Structure
    ObjectFactory obf;

    @Service
    @Optional
    MenuAccessControl menuAccessControl;

    @Service
    @Optional
    I18NProvider i18NProvider;

    TypeLookup typeLookup;

    public Qi4jInstantiator(@Uses VaadinServletService vaadinService, @Structure Module module)
    {
        super(vaadinService);
        this.typeLookup = module.typeLookup();
    }

    @Override
    public <T> T getOrCreate(Class<T> type)
    {
        Qi4jManaged annotation = type.getAnnotation(Qi4jManaged.class);
        if (annotation == null)
        {
            return super.getOrCreate(type);
        }
        return obf.newObject(type);
    }

    public Stream<VaadinServiceInitListener> getServiceInitListeners()
    {
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        Stream<VaadinServiceInitListener> qi4jListeners = typeLookup.allObjects()
            .filter(od -> od.hasType(VaadinServiceInitListener.class))
            .map(od -> (VaadinServiceInitListener) obf.newObject(od.types().findFirst().get()));
        return Stream.concat(super.getServiceInitListeners(), qi4jListeners);
    }

    public <T extends Component> T createComponent(Class<T> componentClass)
    {
        // TODO: Perhaps add to support Transient Composites too?
        if (typeLookup.allObjects().anyMatch(od -> od.hasType(componentClass)))
        {
            return obf.newObject(componentClass);
        }
        return super.createComponent(componentClass);
    }

    public I18NProvider getI18NProvider()
    {
        if (i18NProvider != null)
        {
            return i18NProvider;
        }
        return super.getI18NProvider();
    }

    public MenuAccessControl getMenuAccessControl()
    {
        if (menuAccessControl != null)
        {
            return menuAccessControl;
        }

        return super.getMenuAccessControl();
    }
}

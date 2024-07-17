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
package org.qi4j.library.webx.vaadin;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.object.ObjectFactory;

public class Qi4jServiceInitListener
    implements VaadinServiceInitListener
{
    @Structure
    ObjectFactory obf;

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent)
    {
        // Injecting any references to Qi4j runtime. This disallows constructor injection, but at least we get field injection after instantiation.
        obf.injectTo(serviceInitEvent.getSource());
    }
}

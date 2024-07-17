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
package org.qi4j.library.webx.vaadin.crud.form.impl.field.provider;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import java.util.Collection;
import org.qi4j.library.webx.vaadin.crud.form.FieldProvider;

@SuppressWarnings("rawtypes")
public abstract class AbstractListingProvider<C extends Component & HasListDataView<T, ?> & HasValueAndElement, T>
    implements FieldProvider<C, T>
{
    protected String caption;
    protected Collection<T> items;
    protected ComponentRenderer<? extends Component, T> renderer;

    protected abstract C buildAbstractListing();

    public AbstractListingProvider(Collection<T> items)
    {
        this(null, items, new TextRenderer<>());
    }

    public AbstractListingProvider(String caption, Collection<T> items)
    {
        this(caption, items, new TextRenderer<>());
    }

    public AbstractListingProvider(String caption, Collection<T> items,
                                   ComponentRenderer<? extends Component, T> renderer)
    {
        this.caption = caption;
        this.items = items;
        this.renderer = renderer;
    }

    @Override
    public C buildField(T t)
    {
        C field = buildAbstractListing();
        field.setItems(items);
        return field;
    }
}

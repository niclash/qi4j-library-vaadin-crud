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
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import java.util.Collection;

public class ComboBoxProvider<T> extends AbstractListingProvider<ComboBox<T>, T>
{
    private ItemLabelGenerator<T> itemLabelGenerator;

    public ComboBoxProvider(Collection<T> items)
    {
        super(items);
    }

    public ComboBoxProvider(String caption, Collection<T> items)
    {
        super(caption, items);
    }

    public ComboBoxProvider(String caption, Collection<T> items, ComponentRenderer<? extends Component, T> renderer,
                            ItemLabelGenerator<T> itemLabelGenerator)
    {
        super(caption, items, renderer);
        this.itemLabelGenerator = itemLabelGenerator;
    }

    @Override
    protected ComboBox<T> buildAbstractListing()
    {
        ComboBox<T> field = new ComboBox<>();
        if (renderer != null)
        {
            field.setRenderer(renderer);
        }
        if (itemLabelGenerator != null)
        {
            field.setItemLabelGenerator(itemLabelGenerator);
        }
        field.setItems(items);
        return field;
    }
}

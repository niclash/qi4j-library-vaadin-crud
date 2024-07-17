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
package org.qi4j.library.web.vaadin.crud.form.impl.field.provider;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import java.util.Collection;

public class CheckBoxGroupProvider<T> extends AbstractListingProvider<CheckboxGroup<T>, T>
{
    private ItemLabelGenerator<T> itemLabelGenerator;

    public CheckBoxGroupProvider(Collection<T> items)
    {
        super(items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items)
    {
        super(caption, items);
    }

    public CheckBoxGroupProvider(String caption, Collection<T> items, ItemLabelGenerator<T> itemLabelGenerator)
    {
        super(caption, items);
        this.itemLabelGenerator = itemLabelGenerator;
    }

    @Override
    protected CheckboxGroup<T> buildAbstractListing()
    {
        CheckboxGroup<T> field = new CheckboxGroup<>();
        if (itemLabelGenerator != null)
        {
            field.setItemLabelGenerator(itemLabelGenerator);
        }
        return field;
    }
}

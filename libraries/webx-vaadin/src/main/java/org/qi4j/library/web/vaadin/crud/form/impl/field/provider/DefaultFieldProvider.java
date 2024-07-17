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

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextField;
import java.time.LocalDate;
import java.util.Arrays;
import org.qi4j.library.web.vaadin.crud.form.FieldProvider;

public class DefaultFieldProvider<C extends HasValueAndElement, T> implements FieldProvider<C,T>
{
    private final Class<T> type;

    public DefaultFieldProvider(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public C buildField(T t)
    {
        if (Boolean.class.isAssignableFrom(type) || boolean.class == type)
        {
            return (C) new Checkbox();
        }

        if (LocalDate.class.isAssignableFrom(type))
        {
            return (C) new DatePicker();
        }

        if (Enum.class.isAssignableFrom(type))
        {
            T[] enumConstants = type.getEnumConstants();
            ComboBox<T> comboBox = new ComboBox<>();
            comboBox.setItems(Arrays.asList(enumConstants));
            return (C) comboBox;
        }

        if (String.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)
            || Byte.class.isAssignableFrom(type)
            || Number.class.isAssignableFrom(type) || type.isPrimitive())
        {
            return (C) new TextField();
        }
        return null;
    }
}

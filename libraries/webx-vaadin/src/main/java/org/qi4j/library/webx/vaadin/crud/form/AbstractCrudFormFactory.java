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
package org.qi4j.library.webx.vaadin.crud.form;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import java.util.Arrays;
import java.util.Map;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.qi4j.library.webx.vaadin.crud.CrudOperation;

public abstract class AbstractCrudFormFactory<T>
    implements CrudFormFactory<T>
{
    protected SerializableConsumer<Exception> errorListener;

    protected boolean showNotifications;

    @Override
    public void showProperty(CrudOperation operation, PropertyDescriptor property)
    {
        // TODO
    }

    @Override
    public void showProperty(PropertyDescriptor property)
    {
        Arrays.stream(CrudOperation.values()).forEach(operation -> showProperty(operation, property));
    }

    @Override
    public void disableProperty(CrudOperation operation, PropertyDescriptor property)
    {
        // TODO
    }

    @Override
    public void disabledProperty(PropertyDescriptor property)
    {
        Arrays.stream(CrudOperation.values()).forEach(operation -> disableProperty(operation, property));
    }

    @Override
    public void setFieldCreationListener(CrudOperation operation, PropertyDescriptor property, FieldCreationListener listener)
    {
        // TODO
    }

    @Override
    public void setFieldCreationListener(PropertyDescriptor property, FieldCreationListener listener)
    {
        Arrays.stream(CrudOperation.values())
            .forEach(operation -> setFieldCreationListener(operation, property, listener));
    }

    public Map<PropertyDescriptor, FieldCreationListener> getFieldCreationListeners()
    {
        // TODO
        return null;
    }

    @Override
    public void setFieldProvider(CrudOperation operation, PropertyDescriptor property, FieldProvider<?, ?> provider)
    {
        // TODO
    }

    @Override
    public void setFieldProvider(PropertyDescriptor property, FieldProvider<?, ?> provider)
    {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setFieldProvider(operation, property, provider));
    }

    public Map<PropertyDescriptor, FieldProvider> getFieldProviders()
    {
        // TODO
        return null;
    }

    @Override
    public void setConverter(CrudOperation operation, PropertyDescriptor property, Converter<?, ?> converter)
    {
        // TODO
    }

    @Override
    public void setConverter(PropertyDescriptor property, Converter<?, ?> converter)
    {
        Arrays.stream(CrudOperation.values()).forEach(operation -> setConverter(operation, property, converter));
    }

    public Map<PropertyDescriptor, Converter<?,?>> getConverters()
    {
        // TODO
        return null;
    }

    @Override
    public void setShowNotifications(boolean showNotifications)
    {
        this.showNotifications = showNotifications;
    }

    public void showNotification(String text)
    {
        if (showNotifications)
        {
            Notification.show(text);
        }
    }

    @Override
    public void setErrorListener(SerializableConsumer<Exception> errorListener)
    {
        this.errorListener = errorListener;
    }
}

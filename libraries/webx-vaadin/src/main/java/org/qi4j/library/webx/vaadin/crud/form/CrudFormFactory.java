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

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;
import java.io.Serializable;
import java.util.stream.Stream;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.property.PropertyDescriptor;
import org.qi4j.library.webx.vaadin.crud.CrudOperation;

public interface CrudFormFactory<T> extends Serializable
{
    SerializableSupplier<T> getNewInstanceSupplier();

    Component buildNewForm(CrudOperation operation, T domainObject, boolean readOnly,
                           ComponentEventListener<ClickEvent<Button>> cancelButtonClickListener,
                           ComponentEventListener<ClickEvent<Button>> operationButtonClickListener);

    String buildCaption(CrudOperation operation, T domainObject);

    void showProperty(CrudOperation operation, PropertyDescriptor property);

    void showProperty(PropertyDescriptor property);

    void disableProperty(CrudOperation operation, PropertyDescriptor property);

    void disabledProperty(PropertyDescriptor property);

    void setFieldCreationListener(CrudOperation operation, PropertyDescriptor property, FieldCreationListener listener);

    void setFieldCreationListener(PropertyDescriptor property, FieldCreationListener listener);

    void setFieldProvider(CrudOperation operation, PropertyDescriptor property, FieldProvider<?, ?> provider);

    void setFieldProvider(PropertyDescriptor property, FieldProvider<?, ?> provider);

    void setConverter(CrudOperation operation, PropertyDescriptor property, Converter<?, ?> converter);

    void setConverter(PropertyDescriptor property, Converter<?, ?> converter);

    void setShowNotifications(boolean showNotifications);

    void setErrorListener(SerializableConsumer<Exception> errorListener);

    Stream<? extends PropertyDescriptor> discoverProperties();

    Stream<? extends AssociationDescriptor> discoverAssociations();

    Stream<? extends AssociationDescriptor> discoverManyAssociations();

    Stream<? extends AssociationDescriptor> discoverNamedAssociations();

    void showError(CrudOperation operation, Exception e);
}

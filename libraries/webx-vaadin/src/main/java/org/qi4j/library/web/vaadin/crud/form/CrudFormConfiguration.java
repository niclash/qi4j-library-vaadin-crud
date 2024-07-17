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
package org.qi4j.library.web.vaadin.crud.form;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.data.converter.Converter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.polygene.api.property.PropertyDescriptor;

public class CrudFormConfiguration
{

    protected List<PropertyDescriptor> visibleProperties = new CopyOnWriteArrayList<>();
    protected List<PropertyDescriptor> disabledProperties = new CopyOnWriteArrayList<>();
    protected List<String> fieldCaptions = new ArrayList<>();
    protected Map<Object, Class<? extends HasValueAndElement<?, ?>>> fieldTypes = new HashMap<>();
    protected Map<Object, FieldCreationListener> fieldCreationListeners = new HashMap<>();
    protected Map<Object, FieldProvider<?, ?>> fieldProviders = new HashMap<>();
    protected Map<Object, Converter<?, ?>> converters = new HashMap<>();

//    public List<PropertyDescriptor> getVisibleProperties()
//    {
//        return visibleProperties;
//    }
//
//    public void showProperty(PropertyDescriptor property)
//    {
//        this.visibleProperties.add(property);
//    }
//
//    public void hideProperty(PropertyDescriptor property)
//    {
//        this.visibleProperties.remove(property);
//    }
//
//    public List<PropertyDescriptor> getDisabledProperties()
//    {
//        return disabledProperties;
//    }
//
//    public void disabledProperty(PropertyDescriptor property)
//    {
//        this.disabledProperties.add(property);
//    }
//    public void enableProperty(PropertyDescriptor property)
//    {
//        this.disabledProperties.remove(property);
//    }
//
//    public List<String> getFieldCaptions()
//    {
//        return fieldCaptions;
//    }
//
//    public void setFieldCaptions(List<String> fieldCaptions)
//    {
//        this.fieldCaptions = fieldCaptions;
//    }
//
//    public Map<Object, Class<? extends HasValueAndElement<?, ?>>> getFieldTypes()
//    {
//        return fieldTypes;
//    }
//
//    public void setFieldTypes(Map<Object, Class<? extends HasValueAndElement<?, ?>>> fieldTypes)
//    {
//        this.fieldTypes = fieldTypes;
//    }
//
//    public Map<Object, FieldCreationListener> getFieldCreationListeners()
//    {
//        return fieldCreationListeners;
//    }
//
//    public void setFieldCreationListeners(Map<Object, FieldCreationListener> fieldCreationListeners)
//    {
//        this.fieldCreationListeners = fieldCreationListeners;
//    }
//
//    public Map<Object, FieldProvider<?, ?>> getFieldProviders()
//    {
//        return fieldProviders;
//    }
//
//    public void setFieldProviders(Map<Object, FieldProvider<?, ?>> fieldProviders)
//    {
//        this.fieldProviders = fieldProviders;
//    }
//
//    public Map<Object, Converter<?, ?>> getConverters()
//    {
//        return converters;
//    }
//
//    public void setConverters(Map<Object, Converter<?, ?>> converters)
//    {
//        this.converters = converters;
//    }

}

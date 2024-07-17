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
package org.qi4j.library.webx.vaadin.data.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToCharacterConverter implements Converter<String, Character>
{

    @Override
    public Result<Character> convertToModel(String value, ValueContext context)
    {
        if (value == null)
        {
            return Result.ok(null);
        }

        if (value.length() > 1)
        {
            return Result.error("Could not convert '" + value);
        }

        return Result.ok(value.charAt(0));
    }

    @Override
    public String convertToPresentation(Character value, ValueContext context)
    {
        if (value == null)
        {
            return null;
        }

        return value.toString();
    }

}
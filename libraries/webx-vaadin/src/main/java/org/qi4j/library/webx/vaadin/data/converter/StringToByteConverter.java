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
import com.vaadin.flow.data.converter.AbstractStringToNumberConverter;
import java.text.NumberFormat;
import java.util.Locale;

public class StringToByteConverter extends AbstractStringToNumberConverter<Byte>
{

    /**
     * Creates a new converter instance with the given error message. Empty
     * strings are converted to <code>null</code>.
     *
     * @param errorMessage the error message to use if conversion fails
     */
    public StringToByteConverter(String errorMessage)
    {
        this(null, errorMessage);
    }

    /**
     * Creates a new converter instance with the given empty string value and
     * error message.
     *
     * @param emptyValue   the presentation value to return when converting an empty
     *                     string, may be <code>null</code>
     * @param errorMessage the error message to use if conversion fails
     */
    public StringToByteConverter(Byte emptyValue, String errorMessage)
    {
        super(emptyValue, errorMessage);
    }

    /**
     * Returns the format used by
     * {@link #convertToPresentation(Object, ValueContext)} and
     * {@link #convertToModel(String, ValueContext)}.
     *
     * @param locale The locale to use
     * @return A NumberFormat instance
     */
    @Override
    protected NumberFormat getFormat(Locale locale)
    {
        if (locale == null)
        {
            locale = Locale.getDefault();
        }
        return NumberFormat.getIntegerInstance(locale);
    }

    @Override
    public Result<Byte> convertToModel(String value, ValueContext context)
    {
        Result<Number> n = convertToNumber(value, context);
        return n.flatMap(number -> {
            if (number == null)
            {
                return Result.ok(null);
            }
            else
            {
                byte intValue = number.byteValue();
                if (intValue == number.longValue())
                {
                    // If the value of n is outside the range of long, the
                    // return value of longValue() is either Long.MIN_VALUE or
                    // Long.MAX_VALUE. The/ above comparison promotes int to
                    // long and thus does not need to consider wrap-around.
                    return Result.ok(intValue);
                }
                else
                {
                    return Result.error(getErrorMessage(context));
                }
            }
        });
    }

}

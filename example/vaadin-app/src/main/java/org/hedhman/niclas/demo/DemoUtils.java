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
package org.hedhman.niclas.demo;

import org.apache.commons.lang3.StringUtils;

public class DemoUtils
{

    public static String getViewName(Class clazz)
    {
        String lowerCase = StringUtils.join(
            StringUtils.splitByCharacterTypeCamelCase(clazz.getSimpleName()),
            " ").toLowerCase().replace("view", "").replace("crud", "CRUD");

        return lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
    }

    public static String getGitHubLink(Class clazz)
    {
        return "https://github.com/alejandro-du/crudui/tree/master/crud-ui-demo/src/main/java/"
            + clazz.getName().replace(".", "/")
            + ".java";
    }

}
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
package org.hedhman.niclas.demo.ui.view;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.qi4j.library.web.vaadin.Qi4jManaged;
import org.hedhman.niclas.demo.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@Qi4jManaged
public class HomeView extends VerticalLayout
{
    public HomeView()
    {
        add(
            new H1("Crud UI add-on demo"),
            new Html("<span>" +
                "This is the demo app for the" +
                " <a href='https://vaadin.com/directory/component/crud-ui-add-on'>Crud UI add-on for Vaadin</a>."
                +
                " The full source code for this demo application is" +
                " <a href='https://github.com/alejandro-du/crudui/tree/master/demo'>available on GitHub</a>." +
                " You can find a link to the source code of each specific demo view at the <b>bottom of each view</b>."
                +
                "</span>"));
    }
}

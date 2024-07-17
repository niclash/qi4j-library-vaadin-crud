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
package org.qi4j.library.web.vaadin.crud.layout.impl;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;

public class VerticalSplitCrudLayout extends AbstractTwoComponentsCrudLayout
{
    public VerticalSplitCrudLayout()
    {
        secondComponentHeaderLayout.setMargin(true);
    }

    @Override
    protected SplitLayout buildMainLayout()
    {
        SplitLayout mainLayout = new SplitLayout(firstComponent, secondComponent);
        mainLayout.setOrientation(Orientation.VERTICAL);
        mainLayout.setSizeFull();
        return mainLayout;
    }

    @Override
    protected void addToolbarLayout(Component toolbarLayout)
    {
        firstComponentHeaderLayout.add(toolbarLayout);
    }

    @Override
    public void addToolbarComponent(Component component)
    {
        if (!firstComponentHeaderLayout.isVisible())
        {
            firstComponentHeaderLayout.setVisible(true);
            firstComponent.getElement().insertChild(firstComponent.getComponentCount() - 1,
                firstComponentHeaderLayout.getElement());
        }

        toolbarLayout.setVisible(true);
        toolbarLayout.add(component);
    }
}

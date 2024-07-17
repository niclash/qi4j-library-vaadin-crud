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
package org.hedhman.niclas.demo.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.AbstractStreamResource;
import com.vaadin.flow.server.StreamResource;
import java.util.HashMap;
import java.util.Map;
import org.hedhman.niclas.demo.ui.view.CrudWithFilterView;
import org.hedhman.niclas.demo.ui.view.CrudWithLazyLoadingView;
import org.hedhman.niclas.demo.ui.view.CrudWithSplitLayoutView;
import org.hedhman.niclas.demo.ui.view.HomeView;
import org.hedhman.niclas.demo.ui.view.SimpleCrudView;
import org.hedhman.niclas.demo.ui.view.SimpleTreeCrudView;
import org.hedhman.niclas.demo.DemoUtils;

public class MainLayout extends AppLayout implements BeforeEnterObserver, AfterNavigationObserver
{

    private Tabs tabs = new Tabs();
    private Map<Tab, Class<? extends HasComponents>> tabToView = new HashMap<>();
    private Map<Class<? extends HasComponents>, Tab> viewToTab = new HashMap<>();

    public MainLayout()
    {
        AppLayout appLayout = new AppLayout();

        Image img = new Image("logo.svg", "Logo");
        img.setHeight("44px");
        addToNavbar(img);

        tabs.addSelectedChangeListener(event -> tabsSelectionChanged(event));
        addToNavbar(tabs);

        addTab(HomeView.class);
        addTab(SimpleCrudView.class);
        addTab(CrudWithSplitLayoutView.class);
        addTab(CrudWithFilterView.class);
        addTab(CrudWithLazyLoadingView.class);
        addTab(SimpleTreeCrudView.class);
    }

    private void tabsSelectionChanged(Tabs.SelectedChangeEvent event)
    {
        if (event.isFromClient())
        {
            UI.getCurrent().navigate((Class<? extends Component>) tabToView.get(event.getSelectedTab()));
        }
    }

    private void addTab(Class<? extends HasComponents> clazz)
    {
        Tab tab = new Tab(DemoUtils.getViewName(clazz));
        tabs.add(tab);
        tabToView.put(tab, clazz);
        viewToTab.put(clazz, tab);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        selectTabByCurrentView(event);
    }

    public void selectTabByCurrentView(BeforeEnterEvent event)
    {
        Class<?> viewClass = event.getNavigationTarget();
        tabs.setSelectedTab(viewToTab.get(viewClass));
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event)
    {
        updatePageTitle();
        addSourceCodeAnchorToCurrentView();
    }

    public void updatePageTitle()
    {
        Class<? extends HasComponents> viewClass = tabToView.get(tabs.getSelectedTab());
        UI.getCurrent().getPage().setTitle(DemoUtils.getViewName(viewClass) + " - " + "Crud UI add-on demo");
    }

    public void addSourceCodeAnchorToCurrentView()
    {
        Class<? extends HasComponents> viewClass = tabToView.get(tabs.getSelectedTab());
        if (!HomeView.class.equals(viewClass))
        {
            HorizontalLayout footer = new HorizontalLayout(
                new Anchor(DemoUtils.getGitHubLink(viewClass), "Source code"));
            footer.setMargin(true);
            ((HasComponents) getContent()).add(footer);
        }
    }

}

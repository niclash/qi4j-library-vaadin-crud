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
package org.qi4j.library.webx;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.WeakHashMap;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.usecase.Usecase;
import org.apache.polygene.api.usecase.UsecaseBuilder;

/**
 * This filter wraps all HTTP access with a UnitOfWork.
 */
public class UnitOfWorkFilter
    implements Filter
{
    private final WeakHashMap<Thread, UnitOfWork> uowPool = new WeakHashMap<>();

    @Structure
    UnitOfWorkFactory uowf;

    @SuppressWarnings("resource")
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException
    {
        UnitOfWork uow = uowPool.get(Thread.currentThread());
        if (uow == null)
        {
            uow = createUnitOfWork(servletRequest);
        }
        try
        {
            if (uow.isPaused())
            {
                uow.resume();
            }
            if (!uow.isOpen())
            {
                uow = createUnitOfWork(servletRequest);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
        finally
        {
            if (uow.isOpen())
            {
                uow.pause();
            }
            else
            {
                uowPool.remove(Thread.currentThread());
            }
        }
    }

    private UnitOfWork createUnitOfWork(ServletRequest servletRequest)
    {
        Usecase usecase = UsecaseBuilder.buildUsecase("Long running").withMetaInfo(servletRequest).newUsecase();
        return uowf.newUnitOfWork(usecase);
    }
}

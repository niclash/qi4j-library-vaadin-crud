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
package org.hedhman.niclas.demo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.polygene.api.PolygeneAPI;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.query.QueryExpressions;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;
import org.hedhman.niclas.demo.entity.Technology;

import static org.apache.polygene.api.query.QueryExpressions.templateFor;
import static org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation.Propagation.MANDATORY;

@Mixins(TechnologyService.Mixin.class)
public interface TechnologyService
{
    @UnitOfWorkPropagation(MANDATORY)
    Technology save(Technology technology);

    @UnitOfWorkPropagation(MANDATORY)
    void delete(Technology technology);

    @UnitOfWorkPropagation(MANDATORY)
    List<Technology> findRoots();

    @UnitOfWorkPropagation(MANDATORY)
    List<Technology> findAll();

    @UnitOfWorkPropagation(MANDATORY)
    List<Technology> getLeaves();

    @UnitOfWorkPropagation(MANDATORY)
    Technology findTechnologyById(String technologyId);

    List<Technology> findChildren(Technology technology);

    class Mixin implements TechnologyService
    {
        @Structure
        UnitOfWorkFactory uowf;
        @Structure
        QueryBuilderFactory qbf;
        @Structure
        PolygeneAPI api;

        public Technology save(Technology technology)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            technology = uow.toEntity(Technology.class, technology);
            return technology;
        }

        public void delete(Technology technology)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            uow.remove(technology);
        }

        public List<Technology> findRoots()
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Technology> qb = qbf.newQueryBuilder(Technology.class);
            Technology template = templateFor(Technology.class);
            qb = qb.where(QueryExpressions.isNull(template.parent()));
            Query<Technology> query = uow.newQuery(qb);
            return query.stream().toList();
        }

        public List<Technology> findAll()
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Technology> qb = qbf.newQueryBuilder(Technology.class);
            Technology template = templateFor(Technology.class);
            Query<Technology> query = uow.newQuery(qb);
            return query.stream().toList();
        }

        public List<Technology> getLeaves()
        {
            List<Technology> categories = findAll();

            Set<Technology> parents = new HashSet<>();
            categories.forEach(technology -> {
                Technology parent = technology.parent().get();
                if (parent != null)
                {parents.add(parent);}
            });

            return categories.stream().filter(technology -> {
                return !parents.contains(technology);
            }).collect(Collectors.toList());
        }

        public Technology findTechnologyById(String technologyId)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf(technologyId);
            return uow.get(Technology.class, identity);
        }

        @Override
        public List<Technology> findChildren(Technology technology)
        {
            return technology.children().toList();
        }
    }
}
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

import java.util.List;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.hedhman.niclas.demo.entity.Group;

@Mixins(GroupService.Mixin.class)
public interface GroupService
{

    List<Group> findAll();

    long count();

    Group save(Group group);

    Group createGroup(String name);

    class Mixin implements GroupService
    {

        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        QueryBuilderFactory qbf;

        public List<Group> findAll()
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Group> qb = qbf.newQueryBuilder(Group.class);
            Query<Group> query = uow.newQuery(qb);
            return query.stream().toList();
        }

        public long count()
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<Group> qb = qbf.newQueryBuilder(Group.class);
            Query<Group> query = uow.newQuery(qb);
            return query.count();
        }

        public Group save(Group group)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            return uow.toEntity(Group.class, group);
        }

        @Override
        public Group createGroup(String name)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            EntityBuilder<Group> groupBuilder = uow.newEntityBuilder(Group.class);
            groupBuilder.instance().name().set(name);
            return groupBuilder.newInstance();
        }
    }
}

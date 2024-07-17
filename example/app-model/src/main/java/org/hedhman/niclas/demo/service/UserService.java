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
import org.apache.polygene.api.entity.EntityComposite;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.query.QueryExpressions;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueComposite;
import org.hedhman.niclas.demo.entity.User;

@Mixins(UserService.Mixin.class)
public interface UserService
{
    List<User> findAll();

    List<User> findAllWithPaging(int page, int pageSize);

    long countAll();

    List<User> findByName(String name);

    User save(User user);

    void delete(User user);

    class Mixin
        implements UserService
    {

        @Structure
        QueryBuilderFactory qbf;

        @Structure
        UnitOfWorkFactory uowf;

        @Override
        public List<User> findAll()
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<User> qb = qbf.newQueryBuilder(User.class);
            Query<User> query = uow.newQuery(qb);
            return query.stream().toList();
        }

        @Override
        public List<User> findAllWithPaging(int page, int pageSize)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<User> qb = qbf.newQueryBuilder(User.class);
            Query<User> query = uow.newQuery(qb)
                .firstResult(page * pageSize)
                .maxResults(pageSize);
            return query.stream().toList();
        }

        @Override
        public long countAll()
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<User> qb = qbf.newQueryBuilder(User.class);
            Query<User> query = uow.newQuery(qb);
            return query.count();
        }

        @Override
        public List<User> findByName(String name)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            QueryBuilder<User> qb = qbf.newQueryBuilder(User.class);
            User template = QueryExpressions.templateFor(User.class);
            qb = qb.where(QueryExpressions.eq(template.name(), name));
            Query<User> query = uow.newQuery(qb);
            return query.stream().toList();
        }

        @Override
        public User save(User user)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            if( user instanceof EntityComposite)
                return user;
            if( user instanceof ValueComposite)
                return uow.toEntity(User.class, user);
            throw new InternalError();
        }

        @Override
        public void delete(User user)
        {
            UnitOfWork uow = uowf.currentUnitOfWork();
            if( user instanceof ValueComposite)
            {
                user = uow.get(User.class, user.identity().get());
            }
            if( user instanceof EntityComposite)
            {
                uow.remove(user);
            }
        }
    }
}

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.IdentityGenerator;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.hedhman.niclas.demo.entity.Group;
import org.hedhman.niclas.demo.entity.MaritalStatus;
import org.hedhman.niclas.demo.entity.Technology;
import org.hedhman.niclas.demo.entity.User;
import org.hedhman.niclas.demo.service.GroupService;
import org.hedhman.niclas.demo.service.TechnologyService;
import org.hedhman.niclas.demo.service.UserService;

@Mixins(InitializeDatabase.Mixin.class)
public interface InitializeDatabase
{
    void initialize();

    class Mixin implements InitializeDatabase
    {
        @Service
        IdentityGenerator identityGenerator;

        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        @Service
        GroupService groupService;

        @Service
        UserService userService;

        @Service
        TechnologyService technologyService;

        @Override
        public void initialize()
        {
            try( UnitOfWork uow = uowf.newUnitOfWork(UsecaseBuilder.newUsecase("Init Database")) )
            {

                List<Group> allGroups = Stream.of("Services,IT,HR,Management,Marketing,Sales,Operations,Finance".split(","))
                    .map(groupService::createGroup)
                    .toList();

                String[] firstNames = "Maria,Nicole,Sandra,Brenda,Clare,Cathy,Elizabeth,Tom,John,Daniel,Edward,Hank,Arthur,Bill"
                    .split(",");
                String[] lastNames = "Smith,Johnson,Williams,Jones,Brown,Miller,Wilson,Wright,Thompson,Lee".split(",");

                Random rand = new Random();

                IntStream.rangeClosed(1, 234)
                    .mapToObj(i -> {
                        String name = firstNames[rand.nextInt(firstNames.length)] + " "
                            + lastNames[rand.nextInt(lastNames.length)];
                        ArrayList<Group> groups = IntStream.rangeClosed(1, 1 + rand.nextInt(2))
                            .mapToObj(j -> allGroups.get(rand.nextInt(allGroups.size())))
                            .collect(Collectors.toCollection(ArrayList::new));

                        ValueBuilder<User> builder = vbf.newValueBuilder(User.class);
                        User proto = builder.prototype();
                        Identity identity = identityGenerator.generate(User.class);
                        proto.identity().set(identity);
                        proto.name().set(name);
                        proto.birthDate().set(LocalDate.of(1980 + rand.nextInt(40)-20, rand.nextInt(1,12), rand.nextInt(1,28)));
                        proto.email().set(name.replace(" ", "").toLowerCase() + i + "@test.com");
                        proto.maritalStatus().set(MaritalStatus.values()[rand.nextInt(MaritalStatus.values().length)]);
                        proto.mainGroup().set(groups.get(rand.nextInt(groups.size())));
                        proto.salary().set(BigDecimal.valueOf(rand.nextInt(50000,200000)));
                        groups.forEach( proto.groups()::add );
                        proto.phoneNumber().set(rand.nextInt(9000000) + 1000000);
                        proto.active().set(rand.nextBoolean());
                        proto.password().set("" + rand.nextInt(10000000, 999999999));
                        return builder.newInstance();
                    })
                    .forEach(userService::save);

                String[] techs = new String[]{"Java", "Javascript", "Dart"};
                String[][] components = new String[][]{
                    {"Vaadin", "Spring", "Guice"},
                    {"Hilla", "React", "Svelte"},
                    {"Flutter"}
                };
                for (int i = 0; i < techs.length; i++)
                {
                    ValueBuilder<Technology> builder = vbf.newValueBuilder(Technology.class);
                    Technology proto = builder.prototype();
                    proto.identity().set(identityGenerator.generate(Technology.class));
                    proto.name().set(techs[i]);
                    proto.description().set(techs[i]);
                    Technology tech = builder.newInstance();
                    technologyService.save(tech);
                    for (int j = 0; j < components[i].length; j++)
                    {
                        ValueBuilder<Technology> componentBuilder = vbf.newValueBuilder(Technology.class);
                        Technology p = componentBuilder.prototype();
                        p.identity().set(identityGenerator.generate(Technology.class));
                        p.name().set(components[i][j]);
                        p.description().set(components[i][j]);
                        p.parent().set(tech);
                        technologyService.save(componentBuilder.newInstance());
                    }
                }
                uow.complete();
            }
        }
    }
}

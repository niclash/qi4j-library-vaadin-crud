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
package org.hedhman.niclas.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

public interface User extends HasIdentity
{

    Property<String> name();

    Property<LocalDate> birthDate();

    Property<Integer> phoneNumber(); // as an int for testing purpose()s

    Property<String> email();

    Property<BigDecimal> salary();

    Property<String> password();

    Property<Boolean> active();

    Property<MaritalStatus> maritalStatus();

    Association<Group> mainGroup();

    ManyAssociation<Group> groups();

}

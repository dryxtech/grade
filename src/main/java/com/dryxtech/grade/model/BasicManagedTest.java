/*
 * Copyright (c) 2021 DRYXTECH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dryxtech.grade.model;

import com.dryxtech.grade.api.ManagedTest;
import com.dryxtech.grade.api.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.Map;

/**
 * This class represents a basic managed test.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
public class BasicManagedTest extends BasicTest implements ManagedTest {

    protected @NonNull Map<String, Object> management;

    public BasicManagedTest(Test test, Map<String, Object> management) {
        super(test.getId(), test.getType(), test.getDescription(), test.getName(),
                Collections.unmodifiableList(test.getQuestions()), Collections.unmodifiableMap(test.getReferences()),
                Collections.unmodifiableMap(test.getExtensions()));
        this.management = management;
    }
}


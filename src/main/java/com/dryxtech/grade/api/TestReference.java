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

package com.dryxtech.grade.api;

import java.util.Map;
import java.util.Optional;

/**
 * This interface represents a reference to some item/information related to a test.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
public interface TestReference {

    /**
     * Getter for test reference identification.
     *
     * @return Identification for a test related item
     */
    String getId();

    /**
     * Getter for test reference type.
     *
     * @return Type of a test related item
     */
    String getType();

    /**
     * Getter for test reference description.
     *
     * @return Description of a test related item
     */
    String getDescription();

    /**
     * Getter for test reference extension.
     *
     * @param key attribute lookup name
     * @return Extended attribute of a test related item
     */
    Optional<Object> getExtension(String key);

    /**
     * Getter for all test reference extensions.
     *
     * @return All extended attributes of a test related item
     */
    Map<String, Object> getExtensions();
}

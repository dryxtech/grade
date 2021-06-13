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

import java.util.Collection;
import java.util.function.Predicate;

/**
 * This interface represents a managed group of tests.
 *
 * @param <T> Test Type
 * @author Drew Griffin
 * @since 2021.2
 */
public interface TestBook<T extends Test> {

    /**
     * Add a test to the managed group.
     *
     * @param test to add
     */
    void record(T test);

    /**
     * Add a collection of tests to the managed group.
     *
     * @param tests to add
     */
    void record(Collection<T> tests);

    /**
     * Remove a test from the managed group.
     *
     * @param test to remove
     * @return true if test was found and removed; otherwise false as test was not found
     */
    boolean erase(T test);

    /**
     * Remove tests from the managed group.
     *
     * @param search conditions to control what is removed
     * @return collection of tests that were removed
     */
    Collection<T> erase(Predicate<T> search);

    /**
     * Remove all tests from the managed group.
     *
     * @return collection of tests that were removed
     */
    Collection<T> eraseAll();

    /**
     * Find tests from the managed group.
     *
     * @param search conditions
     * @return collection of tests that were found based on search
     */
    Collection<T> find(Predicate<T> search);

    /**
     * Find all tests from the managed group.
     *
     * @return collection of all tests
     */
    Collection<T> findAll();
}


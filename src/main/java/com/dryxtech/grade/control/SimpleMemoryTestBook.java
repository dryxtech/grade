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

package com.dryxtech.grade.control;

import com.dryxtech.grade.api.Test;
import com.dryxtech.grade.api.TestBook;
import com.dryxtech.grade.util.IdentifierUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A simple, thread-safe in-memory test book.
 *
 * @param <T> Test Type
 * @author Drew Griffin
 * @since 2021.2
 */
public class SimpleMemoryTestBook<T extends Test> implements TestBook<T> {

    protected static final int DEFAULT_MAX_SIZE_LIMIT = 10_000;

    private final String name;
    private final List<T> tests;
    private final int maxSizeLimit;

    public SimpleMemoryTestBook() {
        this(null, null, DEFAULT_MAX_SIZE_LIMIT);
    }

    public SimpleMemoryTestBook(final String name, final List<T> tests, int maxSizeLimit) {

        if (Objects.nonNull(name) && (name.trim().length() > 0)) {
            this.name = name;
        } else {
            this.name = IdentifierUtil.generateIdString();
        }

        if (Objects.nonNull(tests)) {
            if (tests.size() > maxSizeLimit) {
                throw new IllegalArgumentException("maxSizeLimit arg is greater than tests arg current size");
            }
            this.tests = tests;
        } else {
            this.tests = new ArrayList<>();
        }

        this.maxSizeLimit = maxSizeLimit;
    }

    public SimpleMemoryTestBook(final String name) {
        this(name, null, DEFAULT_MAX_SIZE_LIMIT);
    }

    public SimpleMemoryTestBook(final String name, final List<T> tests) {
        this(name, tests, DEFAULT_MAX_SIZE_LIMIT);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public synchronized void record(final T test) {

        Objects.requireNonNull(test, "test must not be null");
        if (this.tests.size() >= maxSizeLimit) {
            throw new IllegalStateException("test book current size is at max limit. cannot record additional test.");
        }

        this.tests.add(test);
    }

    @Override
    public synchronized void record(final Collection<T> tests) {

        Objects.requireNonNull(tests, "tests must not be null");
        if ((this.tests.size() + tests.size()) > maxSizeLimit) {
            throw new IllegalStateException("test book current size + input test count would exceed max limit. cannot record additional tests.");
        }

        this.tests.addAll(tests);
    }

    @Override
    public synchronized boolean erase(final T test) {

        Objects.requireNonNull(test, "test must not be null");

        return this.tests.remove(test);
    }

    @Override
    public synchronized Collection<T> erase(final Predicate<T> search) {

        Objects.requireNonNull(search, "search must not be null");

        List<T> testsRemoved = new ArrayList<>();
        this.tests.stream()
                .filter(search)
                .forEach(testsRemoved::add);

        this.tests.removeAll(testsRemoved);

        return testsRemoved;
    }

    @Override
    public synchronized Collection<T> eraseAll() {

        List<T> tests = new ArrayList<>(this.tests);
        this.tests.clear();
        return tests;
    }

    @Override
    public synchronized Collection<T> find(final Predicate<T> search) {

        if (Objects.isNull(search)) {
            return Collections.emptyList();
        } else {
            return this.tests.stream()
                    .filter(search)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public synchronized Collection<T> findAll() {
        return new ArrayList<>(this.tests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, tests, maxSizeLimit);
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleMemoryTestBook<?> that = (SimpleMemoryTestBook<?>) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(tests, that.tests) &&
                Objects.equals(maxSizeLimit, that.maxSizeLimit);
    }

    @Override
    public String toString() {
        return String.format("SimpleMemoryTestBook{name=%s, maxSizeLimit=%d}", this.name, this.maxSizeLimit);
    }
}


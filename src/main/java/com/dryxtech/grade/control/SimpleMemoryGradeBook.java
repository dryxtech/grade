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

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeBook;
import com.dryxtech.grade.util.IdentifierUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A simple, thread-safe in-memory grade book.
 *
 * @param <T> Grade Type
 * @author Drew Griffin
 * @since 1.0
 */
public class SimpleMemoryGradeBook<T extends Grade> implements GradeBook<T> {

    protected static final int DEFAULT_MAX_SIZE_LIMIT = 10_000;

    private final String name;
    private final List<T> grades;
    private final int maxSizeLimit;

    public SimpleMemoryGradeBook() {
        this(null, null, DEFAULT_MAX_SIZE_LIMIT);
    }

    public SimpleMemoryGradeBook(final String name, final List<T> grades, int maxSizeLimit) {

        if (Objects.nonNull(name) && (name.trim().length() > 0)) {
            this.name = name;
        } else {
            this.name = IdentifierUtil.generateIdString();
        }

        if (Objects.nonNull(grades)) {
            if (grades.size() > maxSizeLimit) {
                throw new IllegalArgumentException("maxSizeLimit arg is greater than grades arg current size");
            }
            this.grades = grades;
        } else {
            this.grades = new ArrayList<>();
        }

        this.maxSizeLimit = maxSizeLimit;
    }

    public SimpleMemoryGradeBook(final String name) {
        this(name, null, DEFAULT_MAX_SIZE_LIMIT);
    }

    public SimpleMemoryGradeBook(final String name, final List<T> grades) {
        this(name, grades, DEFAULT_MAX_SIZE_LIMIT);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public synchronized void record(final T grade) {

        Objects.requireNonNull(grade, "grade must not be null");
        if (this.grades.size() >= maxSizeLimit) {
            throw new IllegalStateException("grade book current size is at max limit. cannot record additional grade.");
        }

        this.grades.add(grade);
    }

    @Override
    public synchronized void record(final Collection<T> grades) {

        Objects.requireNonNull(grades, "grades must not be null");
        if ((this.grades.size() + grades.size()) > maxSizeLimit) {
            throw new IllegalStateException("grade book current size + input grade count would exceed max limit. cannot record additional grades.");
        }

        this.grades.addAll(grades);
    }

    @Override
    public synchronized boolean erase(final T grade) {

        Objects.requireNonNull(grade, "grade must not be null");

        return this.grades.remove(grade);
    }

    @Override
    public synchronized Collection<T> erase(final Predicate<T> search) {

        Objects.requireNonNull(search, "search must not be null");

        List<T> gradesRemoved = new ArrayList<>();
        this.grades.stream()
                .filter(search)
                .forEach(gradesRemoved::add);

        this.grades.removeAll(gradesRemoved);

        return gradesRemoved;
    }

    @Override
    public synchronized Collection<T> find(final Predicate<T> search) {

        if (Objects.isNull(search)) {
            return Collections.emptyList();
        } else {
            return this.grades.stream()
                    .filter(search)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public synchronized Collection<T> findAll() {
        return new ArrayList<>(this.grades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, grades, maxSizeLimit);
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleMemoryGradeBook<?> that = (SimpleMemoryGradeBook<?>) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(grades, that.grades) &&
                Objects.equals(maxSizeLimit, that.maxSizeLimit);
    }

    @Override
    public String toString() {
        return String.format("SimpleMemoryGradeBook{name=%s, maxSizeLimit=%d}", this.name, this.maxSizeLimit);
    }
}

package com.dryxtech.grade.control;

import com.dryxtech.grade.util.IdentifierUtil;
import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeBook;

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

    private final String name;
    private final List<T> grades;

    public SimpleMemoryGradeBook() {
        this(null, null);
    }

    public SimpleMemoryGradeBook(final String name) {
        this(name, null);
    }

    public SimpleMemoryGradeBook(final String name, final List<T> grades) {

        if (Objects.nonNull(name) && (name.trim().length() > 0)) {
            this.name = name;
        } else {
            this.name = IdentifierUtil.generateIdString();
        }

        if (Objects.nonNull(grades)) {
            this.grades = grades;
        } else {
            this.grades = new ArrayList<>();
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public synchronized void record(final T grade) {

        Objects.requireNonNull(grade, "grade must not be null");

        this.grades.add(grade);
    }

    @Override
    public synchronized void record(final Collection<T> grades) {

        Objects.requireNonNull(grades, "grades must not be null");

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
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleMemoryGradeBook<?> that = (SimpleMemoryGradeBook<?>) o;
        return Objects.equals(name, that.name) && Objects.equals(grades, that.grades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, grades);
    }

    @Override
    public String toString() {
        return String.format("SimpleMemoryGradeBook{name=%s}", this.name);
    }
}

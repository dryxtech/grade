package com.dryxtech.grade.api;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * This interface represents a managed group of grades.
 *
 * @param <T> Grade Type
 * @author Drew Griffin
 * @since 1.0
 */
public interface GradeBook<T extends Grade> {

    /**
     * Add a grade to the managed group.
     *
     * @param grade to add
     */
    void record(T grade);

    /**
     * Add a collection of grades to the managed group.
     *
     * @param grades to add
     */
    void record(Collection<T> grades);

    /**
     * Remove a grade from the managed group.
     *
     * @param grade to remove
     * @return true if grade was found and removed; otherwise false as grade was not found
     */
    boolean erase(T grade);

    /**
     * Remove grades from the managed group.
     *
     * @param search conditions to control what is removed
     * @return collection of grades that were removed
     */
    Collection<T> erase(Predicate<T> search);

    /**
     * Find grades from the managed group.
     *
     * @param search conditions
     * @return collection of grades that were found based on search
     */
    Collection<T> find(Predicate<T> search);

    /**
     * Find all grades from the managed group.
     *
     * @return collection of all grades
     */
    Collection<T> findAll();
}

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

/**
 * This interface represents a grader.
 *
 * @param <T> Input Type
 * @author Drew Griffin
 * @since 1.0
 */
@FunctionalInterface
public interface Grader<T> {

    /**
     * Method to grade input and return a grade value.
     *
     * @param t input that grader will grade
     * @return grade value
     * @throws GradeException if grading fails
     */
    GradeValue grade(T t) throws GradeException;
}

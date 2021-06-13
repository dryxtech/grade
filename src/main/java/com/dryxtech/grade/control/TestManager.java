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

import com.dryxtech.grade.api.ManagedTest;
import com.dryxtech.grade.api.Test;
import com.dryxtech.grade.api.TestBook;
import com.dryxtech.grade.api.TestException;
import com.dryxtech.grade.api.TestScore;
import com.dryxtech.grade.api.Tester;
import com.dryxtech.grade.model.BasicManagedTest;
import com.dryxtech.grade.model.BasicTest;
import com.dryxtech.grade.model.BasicTestSubmission;
import com.dryxtech.grade.tester.BasicTester;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class represents a test manager.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@Slf4j
public class TestManager {

    protected final TestBook<ManagedTest> testBook;
    protected final Map<String, Object> managementInfo;

    /**
     * Constructs manager using managementInfo and empty testbook
     *
     * @param managementInfo management information
     */
    public TestManager(Map<String, Object> managementInfo) {
        this(new com.dryxtech.grade.control.SimpleMemoryTestBook<>(), managementInfo);
    }

    /**
     * Constructs manager using passed-in args.
     *
     * @param testBook datasource of tests
     * @param managementInfo management information
     */
    public TestManager(TestBook<ManagedTest> testBook, Map<String, Object> managementInfo) {

        Objects.requireNonNull(testBook, "testBook must not be null");
        Objects.requireNonNull(managementInfo, "management info must not be null");

        this.testBook = testBook;
        this.managementInfo = managementInfo;
    }

    /**
     * Getter of management information for this manager
     *
     * @return management information
     */
    public Map<String, Object> getManagementInformation() {
        return this.managementInfo;
    }

    /**
     * Score a test submission
     *
     * @param test questions
     * @param testSubmission submitted answer
     * @return test score
     * @throws TestException
     */
    public TestScore score(BasicTest test, BasicTestSubmission testSubmission) throws TestException {
        Tester<BasicTestSubmission> tester = new BasicTester(test);
        return tester.score(testSubmission);
    }

    /**
     * Getter of managed test book
     *
     * @return test book
     */
    public TestBook<ManagedTest> getTestBook() {
        return testBook;
    }

    /**
     * Record a test in the test book
     * Note: managementInfo will be determined by the manager
     *
     * @param test test to record
     */
    public void record(final Test test) {
        getTestBook().record(new BasicManagedTest(test, managementInfo));
    }

    /**
     * Record tests in the test book
     * Note: managementInfo will be determined by the manager
     *
     * @param tests tests to record
     */
    public void record(Test... tests) {
        record(Arrays.asList(tests));
    }

    /**
     * Record a collection of tests into test book
     * Note: managementInfo will be determined by the manager
     *
     * @param tests collection of tests
     */
    public void record(final Collection<Test> tests) {
        getTestBook().record(tests.stream()
                .map(test -> new BasicManagedTest(test, managementInfo)).collect(Collectors.toList()));
    }

    /**
     * Finder of tests based on search filter
     *
     * @param search filter
     * @return found tests
     */
    public Collection<Test> find(final Predicate<ManagedTest> search) {
        return new ArrayList<>(this.testBook.find(search));
    }

    /**
     * Getter of all tests in test book
     *
     * @return all tests in managed test book
     */
    public Collection<Test> findAll() {
        return new ArrayList<>(this.testBook.findAll());
    }

    /**
     * Erase a test from test book
     *
     * @param test test to erase
     * @return true if erased; false if not found
     */
    public boolean erase(final Test test) {
        if (Objects.nonNull(test)) {
            return this.testBook.erase(new BasicManagedTest(test, managementInfo));
        }
        return false;
    }

    /**
     * Erase tests in test book using a search filter
     *
     * @param search filter to determine which tests to erase
     * @return collection of tests erased
     */
    public Collection<Test> erase(final Predicate<ManagedTest> search) {
        return new ArrayList<>(this.testBook.erase(search));
    }

    /**
     * Erase all tests in test book
     *
     * @return collection of tests erased
     */
    public Collection<Test> eraseAll() {
        return new ArrayList<>(this.testBook.eraseAll());
    }
}

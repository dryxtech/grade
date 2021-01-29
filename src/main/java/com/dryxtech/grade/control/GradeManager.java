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
import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.api.ManagedGrade;
import com.dryxtech.grade.grader.GradeValueAverageGrader;
import com.dryxtech.grade.grader.GradeWeightedAverageGrader;
import com.dryxtech.grade.grader.GraderFactory;
import com.dryxtech.grade.grader.NumberAverageGrader;
import com.dryxtech.grade.grader.NumberGrader;
import com.dryxtech.grade.model.BasicManagedGrade;
import com.dryxtech.grade.model.GradeBuilder;
import com.dryxtech.grade.model.GradeRank;
import com.dryxtech.grade.model.GradeReferenceBuilder;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.model.GradeValueRangeBuilder;
import com.dryxtech.grade.model.GradingSystemBuilder;
import com.dryxtech.grade.system.GradeConverterNotFoundException;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.system.ZGradingSystem;
import com.dryxtech.grade.util.GradeFileUtil;
import com.dryxtech.grade.util.GradeMathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A controller class for managing the grading process.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class GradeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(GradeManager.class);

    protected final GradingSystemRegistry gradingSystemRegistry;
    protected final GraderFactory graderFactory;
    protected final GradeBook<ManagedGrade> gradeBook;
    protected final Map<String, Object> managementInfo;

    // default grading system graders
    private Grader<Number> defaultGrader;
    private Grader<Collection<Number>> defaultAverageGrader;
    private Grader<Collection<GradeValue>> defaultRollupGrader;
    private Grader<Collection<Grade>> defaultWeightedRollupGrader;

    /**
     * default constructor using empty grade registry, gradebook and management info.
     */
    public GradeManager() {
        this(new GradingSystemRegistry(), new SimpleMemoryGradeBook<>(), new HashMap<>());
    }

    /**
     * Constructs manager using managementInfo and empty grade registry and gradebook
     *
     * @param managementInfo management information
     */
    public GradeManager(final Map<String, Object> managementInfo) {
        this(new GradingSystemRegistry(), new SimpleMemoryGradeBook<>(), managementInfo);
    }

    /**
     * Constructs manager using passed-in args.
     *
     * @param gradingSystemRegistry registry of grading systems
     * @param gradeBook datasource of grades
     * @param managementInfo management information
     */
    public GradeManager(final GradingSystemRegistry gradingSystemRegistry,
                        final GradeBook<ManagedGrade> gradeBook,
                        final Map<String, Object> managementInfo) {

        Objects.requireNonNull(gradingSystemRegistry, "grading system registry must not be null");
        Objects.requireNonNull(gradeBook, "gradebook must not be null");
        Objects.requireNonNull(managementInfo, "management info must not be null");

        this.gradingSystemRegistry = gradingSystemRegistry;
        this.graderFactory = new GraderFactory(gradingSystemRegistry);
        this.gradeBook = gradeBook;
        this.managementInfo = Collections.unmodifiableMap(managementInfo);

        // setup default grading system
        GradingSystem vzGradingSystem = new ZGradingSystem();
        this.gradingSystemRegistry.registerSystem(vzGradingSystem.getId(), vzGradingSystem);
        setDefaultGradingSystem(vzGradingSystem);
    }

    /**
     * Sets default grading system
     *
     * @param gradingSystem system to set as default
     */
    public synchronized void setDefaultGradingSystem(final GradingSystem gradingSystem) {

        registerGradingSystem(GradeConstants.DEFAULT_GRADING_SYSTEM_ID, gradingSystem);

        defaultGrader = new NumberGrader(gradingSystem);
        defaultAverageGrader = new NumberAverageGrader(gradingSystem, gradingSystemRegistry);
        defaultRollupGrader = new GradeValueAverageGrader(gradingSystem, gradingSystemRegistry);
        defaultWeightedRollupGrader = new GradeWeightedAverageGrader(gradingSystem, gradingSystemRegistry);
    }

    /**
     * Register a grading system under a specific name
     *
     * @param key name system is registered under
     * @param gradingSystem system registered
     */
    public void registerGradingSystem(final String key, final GradingSystem gradingSystem) {
        gradingSystemRegistry.registerSystem(key, gradingSystem);
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
     * Load all bundled grading systems into registry
     * registers systems under system id
     *
     * @return true if all systems loaded successfully; otherwise false
     */
    public boolean loadBundledGradingSystems() {

        AtomicInteger failedLoads = new AtomicInteger();
        Arrays.stream(GradeConstants.GRADING_SYSTEM_DEFINITION_FILES).forEach(file -> {
            try {
                GradingSystem gradingSystem = GradeFileUtil.loadBundledGradingSystem(file);
                registerGradingSystem(gradingSystem.getId(), gradingSystem);
            } catch (IOException ex) {
                failedLoads.getAndIncrement();
                LOGGER.error("failed to register grading system file {}", file, ex);
            }
        });

        LOGGER.info("grade manager successfully auto-registered {} grading systems", this.gradingSystemRegistry.getRegisteredSystems().size());
        if (failedLoads.get() > 0) {
            LOGGER.warn("grade manager failed to auto-register {} grading systems", failedLoads);
        }

        return failedLoads.get() == 0;
    }

    /**
     * Getter of grading system registry
     *
     * @return managed grading system registry
     */
    public GradingSystemRegistry getGradingSystemRegistry() {
        return gradingSystemRegistry;
    }

    /**
     * Getter of registered grading systems
     *
     * @return registered grading systems
     */
    public Collection<GradingSystem> getGradingSystems() {
        return gradingSystemRegistry.getRegisteredSystems();
    }

    /**
     * Lookup a registered grading system
     *
     * @param key name the grading system is registered under
     * @return grading system
     * @throws GradingSystemNotFoundException if no grading system is registered under name
     */
    public GradingSystem lookupGradingSystem(final String key) throws GradingSystemNotFoundException {
        return gradingSystemRegistry.lookupSystem(key)
                .orElseThrow(() -> new GradingSystemNotFoundException(key));
    }

    /**
     * Getter of number grader using default grading system
     *
     * @return number grader
     */
    public Grader<Number> getGrader() {
        return defaultGrader;
    }

    /**
     * Getter of number grader using grading system id argument
     *
     * @param gradingSystemId grading system for grader
     * @return number grader
     * @throws GradeException on any failure to create grader
     */
    public Grader<Number> getGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberGrader(gradingSystemId);
    }

    /**
     * Getter of average number grader using default grading system
     *
     * @return average number grader
     */
    public Grader<Collection<Number>> getAverageGrader() {
        return defaultAverageGrader;
    }

    /**
     * Getter of average number grader using grading system id argument
     *
     * @param gradingSystemId grading system for grader
     * @return average number grader
     * @throws GradeException on any failure to create grader
     */
    public Grader<Collection<Number>> getAverageGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberAverageGrader(gradingSystemId);
    }

    /**
     * Getter of grade value rollup grader using default grading system
     *
     * @return grade value rollup grader
     */
    public Grader<Collection<GradeValue>> getRollupGrader() {
        return defaultRollupGrader;
    }

    /**
     * Getter of grade value rollup grader using grading system id argument
     *
     * @param gradingSystemId grading system for grader
     * @return grade value rollup grader
     * @throws GradeException on any failure to create grader
     */
    public Grader<Collection<GradeValue>> getRollupGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeValueAverageGrader(gradingSystemId);
    }

    /**
     * Getter of weighted average rollup grader using default grading system
     *
     * @return weight average rollup grader
     */
    public Grader<Collection<Grade>> getWeightedRollupGrader() {
        return defaultWeightedRollupGrader;
    }

    /**
     * Getter of weighted average rollup grader using grading system id argument
     *
     * @param gradingSystemId grading system for grader
     * @return weight average rollup grader
     * @throws GradeException on any failure to create grader
     */
    public Grader<Collection<Grade>> getWeightedRollupGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeWeightedAverageGrader(gradingSystemId);
    }

    /**
     * Grades a number using default grading system
     *
     * @param value numeric value to grade
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue grade(final Number value) throws GradeException {
        return defaultGrader.grade(value);
    }

    /**
     * Grades a collection of numbers using default grading system
     * The average of the numbers is determined and evaluated with default grading system
     *
     * @param values collection of numbers to grade
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue gradeAverage(final Collection<Number> values) throws GradeException {
        return defaultAverageGrader.grade(values);
    }

    /**
     * Grades a collection of grade values using default grading system
     * The average of the grade values is determined and evaluated with default grading system
     *
     * @param values collection of grade values to grade
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue gradeRollup(final Collection<GradeValue> values) throws GradeException {
        return defaultRollupGrader.grade(values);
    }

    /**
     * Grades a collection of grades using default grading system
     * The weighted average of the grade values is determined and evaluated with default grading system
     * Specific weights should be defined in each grade
     *
     * @param values collection of grades to rollup into a grade
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue gradeWeightedRollup(final Collection<Grade> values) throws GradeException {
        return defaultWeightedRollupGrader.grade(values);
    }

    /**
     * Grades a number using grading system id argument
     *
     * @param value numeric value to grade
     * @param gradingSystemId grading system to use
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue grade(final Number value, final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberGrader(gradingSystemId).grade(value);
    }

    /**
     * Grades a collection of numbers using grading system id argument
     * The average of the numbers is determined and evaluated with grading system
     *
     * @param values collection of numbers to grade
     * @param gradingSystemId grading system to use
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue gradeAverage(final Collection<Number> values, final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberAverageGrader(gradingSystemId).grade(values);
    }

    /**
     * Grades a collection of grade values using grading system id argument
     * The average of the grade values is determined and evaluated with grading system
     *
     * @param values collection of grade values to grade
     * @param gradingSystemId grading system to use
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue gradeRollup(final Collection<GradeValue> values, final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeValueAverageGrader(gradingSystemId).grade(values);
    }

    /**
     * Grades a collection of grades using grading system id argument
     * The weighted average of the grade values is determined and evaluated with grading system
     * Specific weights should be defined in each grade
     *
     * @param values collection of grades to rollup into a grade
     * @param gradingSystemId grading system to use
     * @return grade value
     * @throws GradeException on any failure during the grading process
     */
    public GradeValue gradeWeightedRollup(final Collection<Grade> values, final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeWeightedAverageGrader(gradingSystemId).grade(values);
    }

    /**
     * Ranks a collection of grades from highest to lowest numeric value.
     * Each GradeRank object will contain a numeric rank value (values start at 1).
     * And if grades are of same numeric value then they will have the same rank number.
     *
     * @param grades collection of grades to rank
     * @return sorted collection of ranked grades from highest to lowest
     */
    public Collection<GradeRank> rankHiToLow(final Collection<? extends Grade> grades) {
        return GradeMathUtil.rankGradesHiToLow(grades);
    }

    /**
     * Ranks a collection of grades from lowest to highest numeric value.
     * Each GradeRank object will contain a numeric rank value (values start at 1).
     * And if grades are of same numeric value then they will have the same rank number.
     *
     * @param grades collection of grades to rank
     * @return sorted collection of ranked grades from lowest to highest
     */
    public Collection<GradeRank> rankLowToHigh(final Collection<? extends Grade> grades) {
        return GradeMathUtil.rankGradesLowToHigh(grades);
    }

    /**
     * Calculate the distribution of grades per each distinct grade textValue
     * Note: order of the resulting map values is hi-to-low based on grade numeric value
     *
     * @param grades collection of grades
     * @return grade distribution
     */
    Map<String, Integer> calculateDistribution(final Collection<? extends GradeValue> grades) {
        return GradeMathUtil.calculateGradeDistribution(grades);
    }

    /**
     * Getter of grade value builder
     *
     * @return basic grade value builder
     */
    public GradeValueBuilder getGradeValueBuilder() {
        return GradeValueBuilder.builder();
    }

    /**
     * Getter of grade reference builder
     *
     * @param autoGenerateIds if true then id is auto-generated; otherwise id should be provided before calling build
     * @return basic grade reference builder
     */
    public GradeReferenceBuilder getGradeReferenceBuilder(final boolean autoGenerateIds) {
        return GradeReferenceBuilder.builder(autoGenerateIds);
    }

    /**
     * Getter of grade builder
     *
     * @param autoGenerateIds if true then id is auto-generated; otherwise id should be provided before calling build
     * @return basic grade builder
     */
    public GradeBuilder getGradeBuilder(final boolean autoGenerateIds) {
        return GradeBuilder.builder(autoGenerateIds);
    }

    /**
     * Getter of grading system builder
     *
     * @return basic grading system builder
     */
    public GradingSystemBuilder getGradingSystemBuilder() {
        return GradingSystemBuilder.builder();
    }

    /**
     * Getter of grade value range builder
     *
     * @return basic grade value range builder
     */
    public GradeValueRangeBuilder getGradeValueRangeBuilder() {
        return GradeValueRangeBuilder.builder();
    }

    /**
     * Record a grade in the grade book
     * Note: managementInfo will be determined by the manager
     *
     * @param grade grade to record
     */
    public void record(final Grade grade) {
        getGradeBook().record(new BasicManagedGrade(grade, managementInfo));
    }

    /**
     * Getter of managed grade book
     *
     * @return grade book
     */
    public GradeBook<ManagedGrade> getGradeBook() {
        return gradeBook;
    }

    /**
     * Record a collection of grades into grade book
     * Note: managementInfo will be determined by the manager
     *
     * @param grades collection of grades
     */
    public void record(final Collection<Grade> grades) {
        getGradeBook().record(grades.stream()
                .map(grade -> new BasicManagedGrade(grade, managementInfo)).collect(Collectors.toList()));
    }

    /**
     * Finder of grades based on search filter
     *
     * @param search filter
     * @return found grades
     */
    public Collection<Grade> find(final Predicate<ManagedGrade> search) {
        return new ArrayList<>(this.gradeBook.find(search));
    }

    /**
     * Getter of all grades in grade book
     *
     * @return all grades in managed grade book
     */
    public Collection<Grade> findAll() {
        return new ArrayList<>(this.gradeBook.findAll());
    }

    /**
     * Erase a grade from grade book
     *
     * @param grade grade to erase
     * @return true if erased; false if not found
     */
    public boolean erase(final Grade grade) {
        if (Objects.nonNull(grade)) {
            return this.gradeBook.erase(new BasicManagedGrade(grade, managementInfo));
        }
        return false;
    }

    /**
     * Erase grades in grade book using a search filter
     *
     * @param search filter to determine which grades to erase
     * @return collection of grades erased
     */
    public Collection<Grade> erase(final Predicate<ManagedGrade> search) {
        return new ArrayList<>(this.gradeBook.erase(search));
    }

    /**
     * Register a grade converter in registry
     *
     * @param fromGradingSystem system to convert from
     * @param toGradingSystem system to convert to
     * @param converter grade converter to register
     */
    public void registerConverter(final String fromGradingSystem, final String toGradingSystem,
                                  final GradeConverter converter) {
        gradingSystemRegistry.registerConverter(fromGradingSystem, toGradingSystem, converter);
    }

    /**
     * Lookup a grade converter from registry
     *
     * @param fromGradingSystem system to convert from
     * @param toGradingSystem system to convert to
     * @return grade converter
     * @throws GradeConverterNotFoundException if grade converter not registered
     */
    public GradeConverter lookupConverter(final String fromGradingSystem, final String toGradingSystem) throws GradeConverterNotFoundException {
        return gradingSystemRegistry.lookupConverter(fromGradingSystem, toGradingSystem)
                .orElseThrow(() -> new GradeConverterNotFoundException(fromGradingSystem, toGradingSystem));
    }
}

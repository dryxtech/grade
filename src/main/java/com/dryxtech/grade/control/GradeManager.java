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
public class GradeManager {

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

    public GradeManager() {
        this(new GradingSystemRegistry(), new SimpleMemoryGradeBook<>(), new HashMap<>());
    }

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

    public synchronized void setDefaultGradingSystem(final GradingSystem gradingSystem) {

        registerGradingSystem(GradeConstants.DEFAULT_GRADING_SYSTEM_ID, gradingSystem);

        defaultGrader = new NumberGrader(gradingSystem);
        defaultAverageGrader = new NumberAverageGrader(gradingSystem, gradingSystemRegistry);
        defaultRollupGrader = new GradeValueAverageGrader(gradingSystem, gradingSystemRegistry);
        defaultWeightedRollupGrader = new GradeWeightedAverageGrader(gradingSystem, gradingSystemRegistry);
    }

    // Management

    public void registerGradingSystem(final String key, final GradingSystem gradingSystem) {
        gradingSystemRegistry.registerSystem(key, gradingSystem);
    }

    public GradeManager(final Map<String, Object> managementInfo) {
        this(new GradingSystemRegistry(), new SimpleMemoryGradeBook<>(), managementInfo);
    }

    public Map<String, Object> getManagementInformation() {
        return this.managementInfo;
    }

    // Grading System
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

    public GradingSystemRegistry getGradingSystemRegistry() {
        return gradingSystemRegistry;
    }

    public Collection<GradingSystem> getGradingSystems() {
        return gradingSystemRegistry.getRegisteredSystems();
    }

    public GradingSystem lookupGradingSystem(final String key) throws GradingSystemNotFoundException {
        return gradingSystemRegistry.lookupSystem(key)
                .orElseThrow(() -> new GradingSystemNotFoundException(key));
    }

    // Graders

    public Grader<Number> getGrader() {
        return defaultGrader;
    }

    public Grader<Number> getGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberGrader(gradingSystemId);
    }

    public Grader<Collection<Number>> getAverageGrader() {
        return defaultAverageGrader;
    }

    public Grader<Collection<Number>> getAverageGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberAverageGrader(gradingSystemId);
    }

    public Grader<Collection<GradeValue>> getRollupGrader() {
        return defaultRollupGrader;
    }

    public Grader<Collection<GradeValue>> getRollupGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeValueAverageGrader(gradingSystemId);
    }

    public Grader<Collection<Grade>> getWeightedRollupGrader() {
        return defaultWeightedRollupGrader;
    }

    public Grader<Collection<Grade>> getWeightedRollupGrader(final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeWeightedAverageGrader(gradingSystemId);
    }

    // Grading

    public GradeValue grade(final Number value) throws GradeException {
        return defaultGrader.grade(value);
    }

    public GradeValue gradeAverage(final Collection<Number> values) throws GradeException {
        return defaultAverageGrader.grade(values);
    }

    public GradeValue gradeRollup(final Collection<GradeValue> values) throws GradeException {
        return defaultRollupGrader.grade(values);
    }

    public GradeValue gradeWeightedRollup(final Collection<Grade> values) throws GradeException {
        return defaultWeightedRollupGrader.grade(values);
    }

    public GradeValue grade(final Number value, final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberGrader(gradingSystemId).grade(value);
    }

    public GradeValue gradeAverage(final Collection<Number> values, final String gradingSystemId) throws GradeException {
        return graderFactory.createNumberAverageGrader(gradingSystemId).grade(values);
    }

    public GradeValue gradeRollup(final Collection<GradeValue> values, final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeValueAverageGrader(gradingSystemId).grade(values);
    }

    public GradeValue gradeWeightedRollup(final Collection<Grade> values, final String gradingSystemId) throws GradeException {
        return graderFactory.createGradeWeightedAverageGrader(gradingSystemId).grade(values);
    }

    // Ranking and distributions

    public Collection<GradeRank> rankHiToLow(final Collection<? extends Grade> grades) {
        return GradeMathUtil.rankGradesHiToLow(grades);
    }

    public Collection<GradeRank> rankLowToHigh(final Collection<? extends Grade> grades) {
        return GradeMathUtil.rankGradesLowToHigh(grades);
    }

    Map<String, Integer> calculateDistribution(final Collection<? extends GradeValue> grades) {
        return GradeMathUtil.calculateGradeDistribution(grades);
    }

    // Builders

    public GradeValueBuilder getGradeValueBuilder() {
        return GradeValueBuilder.builder();
    }

    public GradeReferenceBuilder getGradeReferenceBuilder(final boolean autoGenerateIds) {
        return GradeReferenceBuilder.builder(autoGenerateIds);
    }

    public GradeBuilder getGradeBuilder(final boolean autoGenerateIds) {
        return GradeBuilder.builder(autoGenerateIds);
    }

    public GradingSystemBuilder getGradingSystemBuilder() {
        return GradingSystemBuilder.builder();
    }

    public GradeValueRangeBuilder getGradeValueRangeBuilder() {
        return GradeValueRangeBuilder.builder();
    }

    // GradeBook

    public void record(final Grade grade) {
        getGradeBook().record(new BasicManagedGrade(grade, managementInfo));
    }

    public GradeBook<ManagedGrade> getGradeBook() {
        return gradeBook;
    }

    public void record(final Collection<Grade> grades) {
        getGradeBook().record(grades.stream()
                .map(grade -> new BasicManagedGrade(grade, managementInfo)).collect(Collectors.toList()));
    }

    public Collection<Grade> find(final Predicate<ManagedGrade> search) {
        return new ArrayList<>(this.gradeBook.find(search));
    }

    public Collection<Grade> findAll() {
        return new ArrayList<>(this.gradeBook.findAll());
    }

    public boolean erase(final Grade grade) {
        if (Objects.nonNull(grade)) {
            return this.gradeBook.erase(new BasicManagedGrade(grade, managementInfo));
        }
        return false;
    }

    public Collection<Grade> erase(final Predicate<ManagedGrade> search) {
        return new ArrayList<>(this.gradeBook.erase(search));
    }

    // Converters

    public void registerConverter(final String fromGradingSystem, final String toGradingSystem,
                                  final GradeConverter converter) {
        gradingSystemRegistry.registerConverter(fromGradingSystem, toGradingSystem, converter);
    }

    public GradeConverter lookupConverter(final String fromGradingSystem, final String toGradingSystem) throws GradeConverterNotFoundException {
        return gradingSystemRegistry.lookupConverter(fromGradingSystem, toGradingSystem)
                .orElseThrow(() -> new GradeConverterNotFoundException(fromGradingSystem, toGradingSystem));
    }
}

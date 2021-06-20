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

package com.dryxtech.grade.tester;

import com.dryxtech.grade.api.Question;
import com.dryxtech.grade.api.QuestionValue;
import com.dryxtech.grade.api.Test;
import com.dryxtech.grade.api.TestException;
import com.dryxtech.grade.api.TestReference;
import com.dryxtech.grade.api.TestScore;
import com.dryxtech.grade.api.TestSubmission;
import com.dryxtech.grade.api.Tester;
import com.dryxtech.grade.model.BasicQuestionValue;
import com.dryxtech.grade.model.BasicTestScore;
import com.dryxtech.grade.model.BasicTestSubmission;
import com.dryxtech.grade.util.IdentifierUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a basic tester.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
public class BasicTester<T> implements Tester<TestSubmission> {

    private final static BasicQuestionValue DEFAULT_QUESTION_VALUE = BasicQuestionValue.builder()
            .id("default-question-value").value("").weight(BigDecimal.ZERO).build();
    private final Test test;
    private final BigDecimal multiplier;

    public BasicTester(Test test) {
        this(test, null);
    }

    public BasicTester(Test test, BigDecimal multiplier) {
        Objects.requireNonNull(test, "test cannot be null");
        this.test = test;
        this.multiplier = multiplier;
    }

    @Override
    public TestScore score(TestSubmission submission) throws TestException {

        try {
            Objects.requireNonNull(submission, "test submission cannot be null");

            Map<String, TestReference> references = new HashMap<>(submission.getReferences());
            references.putAll(test.getReferences());
            references.putAll(submission.getReferences());
            references.put("test", test);
            references.put("test-submission", submission);

            Map<Question, BigDecimal> results = new HashMap<>();
            test.getQuestions().forEach(question -> {
                String submittedAnswer = submission.getSubmittedAnswers().getOrDefault(question, "");
                QuestionValue value = question.getValue(submittedAnswer).orElse(DEFAULT_QUESTION_VALUE);
                results.put(question, value.getWeight());
            });

            BigDecimal value = getValue(results);
            if (Objects.nonNull(multiplier)) {
                value = value.multiply(multiplier);
            }

            return BasicTestScore.builder()
                    .id(IdentifierUtil.generateIdString())
                    .type(BasicTestSubmission.class.getSimpleName())
                    .description(String.format("test score %s from test containing %d questions", value, test.getQuestions().size()))
                    .questionScores(results)
                    .value(value)
                    .references(references)
                    .build();
        }
        catch (Exception ex) {
            throw new TestException("failed to score test for submission " + submission, ex);
        }
    }

    private BigDecimal getValue(Map<Question, BigDecimal> results) {

        if (results.size() == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal sumOfValues = results.entrySet().stream()
                .filter(Objects::nonNull)
                .map(entry -> entry.getKey().getWeight().multiply(entry.getValue(), MathContext.DECIMAL128))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumOfValues.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        } else {
            return sumOfValues.divide(BigDecimal.valueOf(results.size()), MathContext.DECIMAL128);
        }
    }
}

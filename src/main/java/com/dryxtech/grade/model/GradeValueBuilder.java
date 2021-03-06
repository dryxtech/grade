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

package com.dryxtech.grade.model;

import com.dryxtech.grade.api.GradeValue;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A builder class for basic grade values
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeValueBuilder {

    private BigDecimal numericValue;
    private String textValue;
    private String gradingSystem;

    public GradeValueBuilder() {
    }

    public GradeValueBuilder(final GradeValue gradeValue) {
        this.numericValue = gradeValue.getNumericValue();
        this.textValue = gradeValue.getTextValue();
        this.gradingSystem = gradeValue.getGradingSystem();
    }

    public GradeValueBuilder(final BigDecimal numericValue, final String textValue, final String gradingSystem) {
        this.numericValue = numericValue;
        this.textValue = textValue;
        this.gradingSystem = gradingSystem;
    }

    public static GradeValueBuilder builder() {
        return new GradeValueBuilder();
    }

    public static GradeValueBuilder builder(final GradeValue gradeValue) {
        return new GradeValueBuilder(gradeValue);
    }

    public GradeValueBuilder numericValue(final Number numericValue) {
        if (Objects.nonNull(numericValue)) {
            this.numericValue = new BigDecimal(numericValue.toString());
        } else {
            this.numericValue = null;
        }
        return this;
    }

    public GradeValueBuilder textValue(String textValue) {
        this.textValue = textValue;
        return this;
    }

    public GradeValueBuilder gradingSystem(String gradingSystem) {
        this.gradingSystem = gradingSystem;
        return this;
    }

    public GradeValue build() {
        GradeValue value = new BasicGradeValue(numericValue, textValue, gradingSystem);
        clear();
        return value;
    }

    public GradeValueBuilder clear() {
        numericValue = null;
        textValue = null;
        gradingSystem = null;
        return this;
    }
}

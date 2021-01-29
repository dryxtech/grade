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

package com.dryxtech.grade.system.converter;

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.model.BasicGradeValue;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.util.GradeFileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextValueBasedConverterTest {

    private GradeConverter converter;
    private GradingSystem fromSystem;
    private GradingSystem toSystem;

    @BeforeEach
    void setUp() throws IOException {
        this.fromSystem = GradeFileUtil.loadBundledGradingSystem(GradeConstants.STANDARD_ACADEMIC_SYSTEM + GradeConstants.GRADING_SYSTEM_FILE_EXTENSION);
        this.toSystem = GradeFileUtil.loadBundledGradingSystem(GradeConstants.STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM + GradeConstants.GRADING_SYSTEM_FILE_EXTENSION);

        GradingSystemRegistry registry = new GradingSystemRegistry();
        registry.registerSystem(fromSystem.getId(), fromSystem);
        registry.registerSystem(toSystem.getId(), toSystem);

        converter = new TextValueBasedConverter(registry);
        registry.registerConverter(fromSystem.getId(), toSystem.getId(), converter);
    }

    @Test
    void convert() {

        GradeValue originalValue = new BasicGradeValue(new BigDecimal(96), "A", fromSystem.getId());
        GradeValue convertedValue = assertDoesNotThrow(() -> converter.convert(originalValue, toSystem.getId()));

        assertEquals(96, convertedValue.getNumericValue().intValue());
    }
}

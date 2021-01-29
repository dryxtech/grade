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
import com.dryxtech.grade.system.ZGradingSystem;
import com.dryxtech.grade.system.ZLevel;
import com.dryxtech.grade.util.GradeFileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZSystemToFiveStarConverterTest {

    private GradeConverter converter;
    private GradingSystem fromSystem;
    private GradingSystem toSystem;

    @BeforeEach
    void setUp() throws IOException {
        this.fromSystem = new ZGradingSystem();
        this.toSystem = GradeFileUtil.loadBundledGradingSystem(GradeConstants.STANDARD_FIVE_STAR_SYSTEM + GradeConstants.GRADING_SYSTEM_FILE_EXTENSION);

        GradingSystemRegistry registry = new GradingSystemRegistry();
        registry.registerSystem(fromSystem.getId(), fromSystem);
        registry.registerSystem(toSystem.getId(), toSystem);

        converter = new ZSystemToFiveStarConverter(registry);
        registry.registerConverter(fromSystem.getId(), toSystem.getId(), converter);
    }

    @Test
    void convert() {

        ZLevel zGradeLevel = ZLevel.MEDIUM;
        GradeValue originalValue = new BasicGradeValue(zGradeLevel.getRange().getEndValue(), zGradeLevel.getTextValue(), fromSystem.getId());
        GradeValue convertedValue = assertDoesNotThrow(() -> converter.convert(originalValue, toSystem.getId()));

        assertEquals(3, convertedValue.getNumericValue().intValue());
        assertEquals("3-STAR", convertedValue.getTextValue());
    }
}

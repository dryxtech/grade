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

package com.dryxtech.grade.system;

import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.model.BasicGradingSystem;

import java.util.Arrays;

/**
 * The Z Grading System
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class ZGradingSystem extends BasicGradingSystem {

    public static final String DESCRIPTION = "A percent-based grading system consisting of five primary levels (V to Z)";
    public static final String CATEGORY = "general";
    public static final String TYPE = GradingSystemType.PERCENT.getLabel();
    public static final String NAME = "z-system";
    public static final String VARIANT = "us";

    public ZGradingSystem() {
        super(GradeConstants.Z_GRADING_SYSTEM, DESCRIPTION, CATEGORY, TYPE, NAME, VARIANT, Arrays.asList(
                ZLevel.TOP_PLUS.getRange(),
                ZLevel.TOP.getRange(),
                ZLevel.TOP_MINUS.getRange(),
                ZLevel.HIGH_PLUS.getRange(),
                ZLevel.HIGH.getRange(),
                ZLevel.HIGH_MINUS.getRange(),
                ZLevel.MEDIUM_PLUS.getRange(),
                ZLevel.MEDIUM.getRange(),
                ZLevel.MEDIUM_MINUS.getRange(),
                ZLevel.LOW_PLUS.getRange(),
                ZLevel.LOW.getRange(),
                ZLevel.LOW_MINUS.getRange(),
                ZLevel.BOTTOM_PLUS.getRange(),
                ZLevel.BOTTOM.getRange(),
                ZLevel.BOTTOM_MINUS.getRange())
        );
    }
}

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

import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * This enum represents standard difficulty levels.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
public enum DifficultyLevel {

    EXTREME("Extreme", new BigDecimal("1.10")),
    HARD("Hard", new BigDecimal("1.01")),
    MEDIUM("Medium", BigDecimal.ONE),
    EASY("Easy", new BigDecimal(".99")),
    NONE("", new BigDecimal(".90"));

    private final String label;
    private final BigDecimal weight;

    DifficultyLevel(String label, BigDecimal weight) {
        this.label = label;
        this.weight = weight;
    }

    public static DifficultyLevel of(String label) {

        if (Objects.isNull(label)) {
            return NONE;
        }

        switch (label.toUpperCase()) {

            case "EXTREME":
                return EXTREME;

            case "HARD":
                return HARD;

            case "MEDIUM":
                return MEDIUM;

            case "EASY":
                return EASY;

            default:
                return NONE;
        }
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public BigDecimal getWeight() {
        return weight;
    }
}


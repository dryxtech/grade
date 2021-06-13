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

import com.dryxtech.grade.api.Question;
import com.dryxtech.grade.api.QuestionValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.dryxtech.grade.api.DifficultyLevel.MEDIUM;

/**
 * This class represents a basic question.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Value
@JsonDeserialize(builder = BasicQuestion.QuestionBuilder.class)
public class BasicQuestion implements Question {

    protected @With @NonNull String id;
    @Builder.Default
    protected @NonNull String text = StringUtils.EMPTY;
    @Builder.Default
    protected @NonNull List<QuestionValue> values = new ArrayList<>();
    @Builder.Default
    protected @NonNull BigDecimal weight = MEDIUM.getWeight();
    @Builder.Default
    protected boolean caseSensitive = Boolean.TRUE;

    @Override
    public Optional<QuestionValue> getValue(String value) {
        if (caseSensitive) {
            return values.stream().filter(option -> option.getValue().equals(value)).findFirst();
        } else {
            return values.stream().filter(option -> option.getValue().equalsIgnoreCase(value)).findFirst();
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class QuestionBuilder {
    }
}

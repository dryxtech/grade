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

/**
 * This class represents a basic question option.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Value
@JsonDeserialize(builder = BasicQuestionValue.QuestionValueBuilder.class)
public class BasicQuestionValue implements QuestionValue {

    protected @With @NonNull String id;
    @Builder.Default
    protected @NonNull String value = StringUtils.EMPTY;
    @Builder.Default
    protected BigDecimal weight = BigDecimal.ONE;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class QuestionValueBuilder {
    }
}

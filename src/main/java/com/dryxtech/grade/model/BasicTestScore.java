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
import com.dryxtech.grade.api.TestReference;
import com.dryxtech.grade.api.TestScore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a basic test score.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Value
@JsonDeserialize(builder = BasicTestScore.TestScoreBuilder.class)
public class BasicTestScore implements TestScore {

    protected @With @NonNull String id;
    @Builder.Default
    protected @NonNull String type = BasicTestScore.class.getSimpleName();
    @Builder.Default
    protected @NonNull String description = "test score";
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    protected @NonNull ZonedDateTime timestamp = ZonedDateTime.now(Clock.systemDefaultZone());
    @Builder.Default
    protected @NonNull BigDecimal value = BigDecimal.ZERO;
    @Builder.Default
    protected @NonNull Map<Question, BigDecimal> questionScores = Collections.emptyMap();
    @Builder.Default
    protected Map<String, TestReference> references = Collections.emptyMap();
    @Builder.Default
    protected @NonNull Map<String, Object> extensions = new LinkedHashMap<>();

    @Override
    public Optional<TestReference> getReference(String key) {
        return Optional.of(references.get(key));
    }

    @Override
    public Optional<Object> getExtension(String key) {
        return Optional.of(extensions.get(key));
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TestScoreBuilder {
    }
}

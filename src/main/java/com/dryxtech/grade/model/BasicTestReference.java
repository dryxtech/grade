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

import com.dryxtech.grade.api.TestReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a basic test reference.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Value
@JsonDeserialize(builder = BasicTestReference.TestReferenceBuilder.class)
public class BasicTestReference implements TestReference {

    protected @With @NonNull String id;
    protected @NonNull String type;
    protected @NonNull String description;
    @Builder.Default
    protected @NonNull Map<String, Object> extensions = new HashMap<>();

    @Override
    public Optional<Object> getExtension(String key) {
        return Optional.of(extensions.get(key));
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class TestReferenceBuilder {
    }
}

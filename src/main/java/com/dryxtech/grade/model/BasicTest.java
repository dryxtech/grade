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
import com.dryxtech.grade.api.Test;
import com.dryxtech.grade.api.TestReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a basic test.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
@Data
public class BasicTest implements Test {

    @Builder.Default
    protected @With @NonNull String id = StringUtils.EMPTY;
    @Builder.Default
    protected @NonNull String type = "standard";
    @Builder.Default
    protected @NonNull String description = StringUtils.EMPTY;
    @Builder.Default
    protected @NonNull String name = StringUtils.EMPTY;
    @Builder.Default
    protected @NonNull List<Question> questions = Collections.emptyList();
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
}

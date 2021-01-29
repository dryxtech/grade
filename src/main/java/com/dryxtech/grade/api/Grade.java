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

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents a grade.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface Grade extends GradeValue, GradeReference {

    /**
     * Getter of grade timestamp.
     *
     * @return Timestamp of grade
     */
    ZonedDateTime getTimestamp();

    /**
     * Getter of grade weight.
     *
     * @return Weight of grade
     */
    BigDecimal getWeight();

    /**
     * Getter for grade reference.
     *
     * @param key reference lookup name
     * @return A references to a grade related item
     */
    Optional<GradeReference> getReference(String key);

    /**
     * Getter for all grade references.
     *
     * @return All references for a grade
     */
    Map<String, GradeReference> getReferences();
}

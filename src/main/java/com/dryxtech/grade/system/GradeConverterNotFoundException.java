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

import com.dryxtech.grade.api.GradeException;

/**
 * An exception class for when grade converter is not found
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeConverterNotFoundException extends GradeException {

    private final String fromGradingSystemId;
    private final String toGradingSystemId;

    public GradeConverterNotFoundException(String fromGradingSystemId, String toGradingSystemId) {
        super("grade converter not found");
        this.fromGradingSystemId = fromGradingSystemId;
        this.toGradingSystemId = toGradingSystemId;
    }

    public String getFromGradingSystemId() {
        return fromGradingSystemId;
    }

    public String getToGradingSystemId() {
        return toGradingSystemId;
    }
}

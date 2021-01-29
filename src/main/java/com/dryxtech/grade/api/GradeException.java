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

/**
 * The parent class for all grade related exceptions.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeException extends Exception {

    public GradeException(final String message) {
        super(message);
    }

    public GradeException(final String message, final Throwable t) {
        super(message, t);
    }
}

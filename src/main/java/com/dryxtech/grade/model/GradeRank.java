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

import com.dryxtech.grade.api.Grade;

import java.util.Objects;

public class GradeRank implements Comparable<GradeRank> {

    private final Grade grade;
    private long rank = 0L;

    public GradeRank(Grade grade, long rank) {
        this.grade = grade;
        this.rank = rank;
    }

    public Grade getGrade() {
        return grade;
    }

    @Override
    public int compareTo(GradeRank gradeRank) {

        if (Objects.isNull(gradeRank)) {
            return -1;
        }

        return Long.compare(getRank(), gradeRank.getRank());
    }

    public long getRank() {
        return rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade, rank);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradeRank gradeRank = (GradeRank) o;
        return rank == gradeRank.rank && Objects.equals(grade, gradeRank.grade);
    }

    @Override
    public String toString() {
        return "GradeRank{" +
                "rank=" + rank +
                ", grade=" + grade +
                '}';
    }
}

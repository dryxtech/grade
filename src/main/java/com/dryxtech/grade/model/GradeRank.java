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

package com.Sarthak.course_recommender.model.enums;

import java.util.List;

public enum SubjectGroup {
    COMPUTER_PHYSICS,
    ACCOUNTS_ECONOMICS;

    public List<Subject> getSubjects() {
        return switch (this) {
            case COMPUTER_PHYSICS -> List.of(
                    Subject.MATHS, Subject.ENGLISH,
                    Subject.SOCIAL, Subject.COMPUTER,
                    Subject.PHYSICS);
            case ACCOUNTS_ECONOMICS -> List.of(
                    Subject.MATHS, Subject.ENGLISH,
                    Subject.SOCIAL, Subject.ACCOUNTS,
                    Subject.ECONOMICS);
        };
    }

    public List<Section> getSections() {
        return switch (this) {
            case COMPUTER_PHYSICS -> List.of(Section.A, Section.B, Section.C);
            case ACCOUNTS_ECONOMICS -> List.of(Section.D, Section.E);
        };
    }

    public static SubjectGroup fromSection(Section section) {
        for (SubjectGroup sg : values()) {
            if (sg.getSections().contains(section)) {
                return sg;
            }
        }
        throw new IllegalArgumentException("No SubjectGroup for section: " + section);
    }
}

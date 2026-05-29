package com.Sarthak.course_recommender.model.enums;

public enum Subject {

    // Common — all sections
    MATHS,
    ENGLISH,
    SOCIAL,

    // Computer/Physics stream only
    COMPUTER,
    PHYSICS,

    // Accounts/Economics stream only
    ACCOUNTS,
    ECONOMICS;

    public boolean isCommonSubject() {
        return this == MATHS || this == ENGLISH || this == SOCIAL;
    }

    public boolean belongsToStream(SubjectGroup group) {
        return switch (group) {
            case COMPUTER_PHYSICS   -> this == COMPUTER || this == PHYSICS  || isCommonSubject();
            case ACCOUNTS_ECONOMICS -> this == ACCOUNTS || this == ECONOMICS || isCommonSubject();
        };
    }
}
package com.Sarthak.course_recommender.model.enums;

public enum Section {
    A , B , C , D , E;

    public SubjectGroup getSubjectGroup()
    {
        return switch(this)
        {
            case A,B,C -> SubjectGroup.COMPUTER_PHYSICS;
            case D , E -> SubjectGroup.ACCOUNTS_ECONOMICS;
        };
    }
}

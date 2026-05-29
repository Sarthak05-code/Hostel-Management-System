package com.Sarthak.course_recommender.service.exception;

public class DormFullException extends RuntimeException {
    public DormFullException(String dormName) {
        super("Dorm '" + dormName + "' is at full capacity (25 students).");
    }
}

package com.Sarthak.course_recommender.service.exception;

public class GenderMismatchException extends RuntimeException {
    public GenderMismatchException(String message) {
        super(message);
    }
}

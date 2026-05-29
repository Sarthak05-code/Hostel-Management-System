package com.Sarthak.course_recommender.service.exception;

public class HostelCapacityExceededException extends RuntimeException {
    public HostelCapacityExceededException() {
        super("Hostel has reached maximum capacity of 200 students.");
    }
}

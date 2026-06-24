package com.Sarthak.course_recommender;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SimpleUnitTest {
    @Test
    void testCourseNameConcatenation() {
        String prefix = "Java";
        String suffix = "101";
        String fullName = prefix + " " + suffix;

        assertEquals("Java 101", fullName , "The course name should match");
    }
    
}

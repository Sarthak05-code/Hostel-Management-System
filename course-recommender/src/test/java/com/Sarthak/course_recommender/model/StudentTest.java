package com.Sarthak.course_recommender.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;

public class StudentTest {

    @Test
    void shouldDeriveSubjectGroupFromSelection() {
        // 1. Arrange : Create a real student using one lombok builder
        Student student = Student.builder()
                .name("Test Subject")
                .gender(Gender.MALE)
                .section(Section.A)
                .build();
        // 2 . Act : Call your actual custom method
        SubjectGroup associatedGroup = student.getSubjectGroup();

        // 3 . Assert : Verify your Logic works perfectly
        assertNotNull(associatedGroup , "Subject group should be not null. ");
        assertEquals(Section.A.getSubjectGroup() , associatedGroup , "The student group must match whatever thier section maps to:");
    }
}

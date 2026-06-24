package com.Sarthak.course_recommender.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;

public class StudentTest {

    // Test 1 : Student Group Logic

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

    // Test 2 : Verifying the custom Lombok equality logic
    // this will test that if the id of 2 student somehow match , they will be considered identical even if they have different name .

    @Test
    void studentsWithSameIdShouldBeEqual() {
        // Arrange : Create 2 student object with the exact same id but unique name
        Student student1 = Student.builder()
                .id(27L)
                .name("Sarthak")
                .gender(Gender.MALE)
                .section(Section.A)
                .build();

        Student student2 = Student.builder()
                .id(27L)
                .name("Sneha") // new name
                .gender(Gender.FEMALE) // new gender
                .section(Section.B) // new class
                .build();
        // Act and Assert : Verify your lombok configurations works exactly as intended
        assertEquals(student1, student2 , "Student with the same id msut be equally according to the rule i have added. ");
    }

}

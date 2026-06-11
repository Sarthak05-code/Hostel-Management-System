package com.Sarthak.course_recommender.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Dorm
    List<Student> findByDorm(Dorm dorm);
    long countByDorm(Dorm dorm);

    

    // Gender
    List<Student> findByGender(Gender gender);
    long countByGender(Gender gender);

    // Section
    List<Student> findBySection(Section section);

    // Stream — pass List.of(A,B,C) or List.of(D,E)
    @Query("SELECT s FROM Student s WHERE s.section IN :sections")
    List<Student> findBySections(@Param("sections") List<Section> sections);

    @Query("SELECT s FROM Student s WHERE s.section IN :sections AND s.gender = :gender")
    List<Student> findBySectionsAndGender(@Param("sections") List<Section> sections,
                                          @Param("gender") Gender gender);

    // Class group
    List<Student> findByClassGroup(ClassGroup classGroup);
    List<Student> findByClassGroupIsNull();
    List<Student> findByDormIsNull();
    long countByClassGroupAndGender(ClassGroup classGroup, Gender gender);

    // Hostel-wide total
    long countBy();
}
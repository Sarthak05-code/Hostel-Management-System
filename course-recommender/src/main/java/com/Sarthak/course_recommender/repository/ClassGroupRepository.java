package com.Sarthak.course_recommender.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {

    List<ClassGroup> findBySubjectGroup(SubjectGroup subjectGroup);
    Optional<ClassGroup> findByGroupName(String groupName);
    void deleteBySubjectGroup(SubjectGroup subjectGroup);

    @Query("SELECT cg FROM ClassGroup cg WHERE SIZE(cg.students) < :maxSize")
    List<ClassGroup> findGroupsUnderCapacity(@Param("maxSize") int maxSize);

    @Query("SELECT cg.groupName, SIZE(cg.students) FROM ClassGroup cg")
    List<Object[]> countStudentsPerGroup();
}
package com.Sarthak.course_recommender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.enums.Gender;

import java.util.List;
import java.util.Optional;

@Repository
public interface DormRepository extends JpaRepository<Dorm, Long> {

    List<Dorm> findByGender(Gender gender);

    Optional<Dorm> findByName(String name);

    @Query("SELECT d FROM Dorm d WHERE SIZE(d.students) < :maxCapacity AND d.gender = :gender")
    List<Dorm> findAvailableDormsByGender(@Param("gender") Gender gender,
            @Param("maxCapacity") int maxCapacity);

    @Query("SELECT d FROM Dorm d WHERE SIZE(d.students) > :maxCapacity")
    List<Dorm> findOverCapacityDorms(@Param("maxCapacity") int maxCapacity);

    @Query("SELECT d FROM Dorm d LEFT JOIN FETCH d.supervisor")
    List<Dorm> findAllWithSupervisor();

    @Query("SELECT d FROM Dorm d LEFT JOIN FETCH d.supervisor WHERE d.id = :id")
    Optional<Dorm> findByIdWithSupervisor(@Param("id") Long id);
}
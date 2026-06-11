package com.Sarthak.course_recommender.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.Supervisor;
import com.Sarthak.course_recommender.model.enums.Gender;

import java.util.List;
import java.util.Optional;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {

    List<Supervisor> findByGender(Gender gender);
    Optional<Supervisor> findByDorm(Dorm dorm);
    List<Supervisor> findByDormIsNull();
}
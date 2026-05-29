package com.Sarthak.course_recommender.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.TimetableSlot;
import com.Sarthak.course_recommender.model.enums.ActivityType;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableSlotRepository extends JpaRepository<TimetableSlot, Long> {

    List<TimetableSlot> findByClassGroup(ClassGroup classGroup);
    List<TimetableSlot> findByClassGroupIsNull();
    List<TimetableSlot> findByDayOfWeek(DayOfWeek dayOfWeek);
    List<TimetableSlot> findByClassGroupAndDayOfWeek(ClassGroup classGroup, DayOfWeek dayOfWeek);
    List<TimetableSlot> findByActivityType(ActivityType activityType);

    @Query("""
            SELECT COUNT(t) > 0 FROM TimetableSlot t
            WHERE t.dayOfWeek = :day
            AND t.classGroup = :classGroup
            AND t.startTime < :endTime
            AND t.endTime > :startTime
            """)
    boolean existsOverlappingSlot(@Param("day") DayOfWeek day,
                                  @Param("classGroup") ClassGroup classGroup,
                                  @Param("startTime") LocalTime startTime,
                                  @Param("endTime") LocalTime endTime);

    void deleteByClassGroup(ClassGroup classGroup);
    void deleteByClassGroupIsNull();
}
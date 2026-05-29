package com.Sarthak.course_recommender.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.Sarthak.course_recommender.model.enums.ActivityType;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString(exclude = "classGroup")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TimetableSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    @Column(nullable = false)
    private boolean weeklyRecurring;

    // null = hostel-wide slot (DINNER, GAMES, MOVIE_NIGHT, FREE_TIME, SLEEP)
    // non-null = class-specific slot (CLASS)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_group_id")
    private ClassGroup classGroup;
}

package com.Sarthak.course_recommender.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.Sarthak.course_recommender.model.enums.SubjectGroup;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString(exclude = {"students", "timetableSlots"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String groupName; // e.g. "CPE-1", "AE-3"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubjectGroup subjectGroup;

    @OneToMany(mappedBy = "classGroup")
    @Builder.Default
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "classGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TimetableSlot> timetableSlots = new ArrayList<>();
}

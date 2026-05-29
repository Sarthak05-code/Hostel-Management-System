package com.Sarthak.course_recommender.model;
import java.util.List;
import java.util.ArrayList;

import com.Sarthak.course_recommender.model.enums.Gender;

import jakarta.persistence.*;
import lombok.*;



@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"supervisor", "students"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dorm {

    public static final int MAX_CAPACITY = 25;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g. "Male Dorm 1", "Female Dorm 2"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @OneToOne(mappedBy = "dorm", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Supervisor supervisor;

    @OneToMany(mappedBy = "dorm")
    @Builder.Default
    private List<Student> students = new ArrayList<>();
}
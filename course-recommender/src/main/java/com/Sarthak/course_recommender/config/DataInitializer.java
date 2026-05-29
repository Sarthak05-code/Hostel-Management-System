package com.Sarthak.course_recommender.config;

import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.Supervisor;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.repository.DormRepository;
import com.Sarthak.course_recommender.repository.SupervisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

        private final DormRepository dormRepository;
        private final SupervisorRepository supervisorRepository;

        private static final List<String> MALE_SUPERVISOR_NAMES = List.of(
                        "Spinos Patroclus",
                        "Gensei Hikumara",
                        "Harishun West",
                        "Xender Sension");

        private static final List<String> FEMALE_SUPERVISOR_NAMES = List.of(
                        "Persa Maynor",
                        "Kasthene Vetsa",
                        "Lauron Benedict",
                        "Samara Hanet");

        @Override
        public void run(String... args) {
                if (dormRepository.count() == 0) {

                        // ── Male dorms + supervisors ─────────────────────
                        for (int i = 0; i < 2; i++) {
                                Dorm dorm = dormRepository.save(Dorm.builder()
                                                .name("Male Dorm " + (i + 1))
                                                .gender(Gender.MALE)
                                                .build());

                                Supervisor supervisor = supervisorRepository.save(Supervisor.builder()
                                                .name(MALE_SUPERVISOR_NAMES.get(i))
                                                .gender(Gender.MALE)
                                                .dorm(dorm)
                                                .build());

                                System.out.println("✓ " + dorm.getName() +
                                                " created → assigned to " + supervisor.getName());
                        }

                        // ── Female dorms + supervisors ───────────────────
                        for (int i = 0; i < 2; i++) {
                                Dorm dorm = dormRepository.save(Dorm.builder()
                                                .name("Female Dorm " + (i + 1))
                                                .gender(Gender.FEMALE)
                                                .build());

                                Supervisor supervisor = supervisorRepository.save(Supervisor.builder()
                                                .name(FEMALE_SUPERVISOR_NAMES.get(i))
                                                .gender(Gender.FEMALE)
                                                .dorm(dorm)
                                                .build());

                                System.out.println("✓ " + dorm.getName() +
                                                " created → assigned to " + supervisor.getName());
                        }

                        System.out.println("✓ 4 dorms and 4 supervisors initialized.");
                }
        }
}
package com.Sarthak.course_recommender.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Sarthak.course_recommender.repository.ClassGroupRepository;
import com.Sarthak.course_recommender.repository.DormRepository;
import com.Sarthak.course_recommender.repository.NoticeRepository;
import com.Sarthak.course_recommender.repository.StudentRepository;
import com.Sarthak.course_recommender.repository.SupervisorRepository;

@Controller
@RequiredArgsConstructor
public class DashboardController {

        private final StudentRepository studentRepository;
        private final DormRepository dormRepository;
        private final ClassGroupRepository classGroupRepository;
        private final SupervisorRepository supervisorRepository;
        private final NoticeRepository noticeRepository;

        @GetMapping("/")
        public String dashboard(Model model) {
                // existing attributes
                model.addAttribute("totalStudents", studentRepository.countBy());
                model.addAttribute("totalDorms", dormRepository.count());
                model.addAttribute("totalClasses", classGroupRepository.count());
                model.addAttribute("totalSupervisors", supervisorRepository.count());
                model.addAttribute("unassignedSupervisors",
                                supervisorRepository.findByDormIsNull().size());
                model.addAttribute("unassignedStudents",
                                studentRepository.findByClassGroupIsNull().size());
                model.addAttribute("overCapacityDorms",
                                dormRepository.findOverCapacityDorms(25).size());

                // notice
                noticeRepository.findAllByOrderByPostedAtDesc()
                                .stream().findFirst()
                                .ifPresent(n -> model.addAttribute("latestNotice", n));


                return "dashboard/index";
        }

}
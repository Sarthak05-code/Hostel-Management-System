package com.Sarthak.course_recommender.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Sarthak.course_recommender.service.StudentGeneratorService;



@Controller
@RequestMapping("/generate-students")
@RequiredArgsConstructor
public class StudentGeneratorController {

    private final StudentGeneratorService studentGeneratorService;

    @GetMapping
    public String showGeneratorPage() {
        return "students/generate";
    }

    @PostMapping
    public String generate(@RequestParam int count,
                           RedirectAttributes redirectAttributes) {
        if (count < 1 || count > 200) {
            redirectAttributes.addFlashAttribute("error", "Count must be between 1 and 200.");
            return "redirect:/generate-students";
        }

        int added = studentGeneratorService.generateStudents(count);
        redirectAttributes.addFlashAttribute("success",
                added + " students generated and assigned to dorms.");
        return "redirect:/students";
    }
}
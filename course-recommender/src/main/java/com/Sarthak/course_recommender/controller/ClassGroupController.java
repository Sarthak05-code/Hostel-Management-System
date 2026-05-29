package com.Sarthak.course_recommender.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;
import com.Sarthak.course_recommender.service.ClassGroupService;

@Controller
@RequestMapping("/classes")
@RequiredArgsConstructor
public class ClassGroupController {

    private final ClassGroupService classGroupService;

    @GetMapping
    public String listClassGroups(Model model) {
        model.addAttribute("classGroups", classGroupService.getAllClassGroups());
        model.addAttribute("summary", classGroupService.getClassSizeSummary());
        model.addAttribute("streams", SubjectGroup.values());
        return "classes/list";
    }

    @GetMapping("/{id}")
    public String viewClassGroup(@PathVariable Long id, Model model) {
        ClassGroup group = classGroupService.getClassGroupById(id);

        long maleCount = group.getStudents().stream()
                .filter(s -> s.getGender() == Gender.MALE)
                .count();
        long femaleCount = group.getStudents().stream()
                .filter(s -> s.getGender() == Gender.FEMALE)
                .count();

        model.addAttribute("classGroup", group);
        model.addAttribute("maleCount", maleCount);
        model.addAttribute("femaleCount", femaleCount);
        return "classes/detail";
    }

    @PostMapping("/generate")
    public String generateClasses(RedirectAttributes redirectAttributes) {
        classGroupService.generateClasses();
        redirectAttributes.addFlashAttribute("success",
                "Classes generated successfully.");
        return "redirect:/classes";
    }

    @PostMapping("/{id}/delete")
    public String deleteClassGroup(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        classGroupService.deleteClassGroup(id);
        redirectAttributes.addFlashAttribute("success", "Class group deleted.");
        return "redirect:/classes";
    }
}
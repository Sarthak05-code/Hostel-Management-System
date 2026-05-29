package com.Sarthak.course_recommender.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Sarthak.course_recommender.model.Supervisor;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.service.DormService;
import com.Sarthak.course_recommender.service.SupervisorService;

@Controller
@RequestMapping("/supervisors")
@RequiredArgsConstructor
public class SupervisorController {

    private final SupervisorService supervisorService;
    private final DormService dormService;

    @GetMapping
    public String listSupervisors(Model model) {
        model.addAttribute("supervisors", supervisorService.getAllSupervisors());
        model.addAttribute("unassigned", supervisorService.getUnassignedSupervisors());
        return "supervisors/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("supervisor", new Supervisor());
        model.addAttribute("genders", Gender.values());
        return "supervisors/form";
    }

    @PostMapping("/add")
    public String addSupervisor(@ModelAttribute Supervisor supervisor,
                                RedirectAttributes redirectAttributes) {
        supervisorService.addSupervisor(supervisor);
        redirectAttributes.addFlashAttribute("success", "Supervisor added successfully.");
        return "redirect:/supervisors";
    }

    @GetMapping("/{id}/assign-dorm")
    public String showAssignDormForm(@PathVariable Long id, Model model) {
        Supervisor supervisor = supervisorService.getSupervisorById(id);
        model.addAttribute("supervisor", supervisor);
        model.addAttribute("availableDorms",
                dormService.getDormsByGender(supervisor.getGender()));
        return "supervisors/assign-dorm";
    }

    @PostMapping("/{id}/assign-dorm")
    public String assignDorm(@PathVariable Long id,
                             @RequestParam Long dormId,
                             RedirectAttributes redirectAttributes) {
        supervisorService.assignSupervisorToDorm(id, dormId);
        redirectAttributes.addFlashAttribute("success", "Supervisor assigned to dorm.");
        return "redirect:/supervisors";
    }

    @PostMapping("/{id}/remove-dorm")
    public String removeDorm(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        supervisorService.removeSupervisorFromDorm(id);
        redirectAttributes.addFlashAttribute("success", "Supervisor unassigned from dorm.");
        return "redirect:/supervisors";
    }

    @PostMapping("/{id}/delete")
    public String deleteSupervisor(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        supervisorService.removeSupervisor(id);
        redirectAttributes.addFlashAttribute("success", "Supervisor removed.");
        return "redirect:/supervisors";
    }
}
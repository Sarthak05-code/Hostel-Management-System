package com.Sarthak.course_recommender.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.service.DormService;

@Controller
@RequestMapping("/dorms")
@RequiredArgsConstructor
public class DormController {

    private final DormService dormService;

    @GetMapping
    public String listDorms(Model model) {
        model.addAttribute("dorms", dormService.getAllDorms());
        model.addAttribute("overCapacity", dormService.getOverCapacityDorms());
        return "dorms/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("dorm", new Dorm());
        model.addAttribute("genders", Gender.values());
        return "dorms/form";
    }

    @PostMapping("/add")
    public String addDorm(@ModelAttribute Dorm dorm,
                          RedirectAttributes redirectAttributes) {
        dormService.createDorm(dorm);
        redirectAttributes.addFlashAttribute("success", "Dorm created successfully.");
        return "redirect:/dorms";
    }

    @GetMapping("/{id}")
    public String viewDorm(@PathVariable Long id, Model model) {
        model.addAttribute("dorm", dormService.getDormById(id));
        model.addAttribute("occupancy", dormService.getOccupancy(id));
        model.addAttribute("capacity", Dorm.MAX_CAPACITY);
        return "dorms/detail";
    }

    @PostMapping("/{id}/rename")
    public String renameDorm(@PathVariable Long id,
                             @RequestParam String newName,
                             RedirectAttributes redirectAttributes) {
        dormService.updateDormName(id, newName);
        redirectAttributes.addFlashAttribute("success", "Dorm renamed.");
        return "redirect:/dorms/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteDorm(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        dormService.deleteDorm(id);
        redirectAttributes.addFlashAttribute("success", "Dorm deleted.");
        return "redirect:/dorms";
    }
}
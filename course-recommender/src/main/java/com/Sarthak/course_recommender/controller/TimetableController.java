package com.Sarthak.course_recommender.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Sarthak.course_recommender.service.TimetableService;

import java.time.DayOfWeek;

@Controller
@RequestMapping("/timetable")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping
    public String viewTimetable(Model model) {
        model.addAttribute("hostelSlots", timetableService.getHostelWideTimetable());
        model.addAttribute("days", DayOfWeek.values());
        return "timetable/view";
    }

    @GetMapping("/class/{classGroupId}")
    public String viewClassTimetable(@PathVariable Long classGroupId, Model model) {
        model.addAttribute("slots", timetableService.getClassGroupTimetable(classGroupId));
        model.addAttribute("classGroupId", classGroupId);
        model.addAttribute("days", DayOfWeek.values());
        return "timetable/class-view";
    }

    @GetMapping("/day/{day}")
    public String viewDailySchedule(@PathVariable DayOfWeek day, Model model) {
        model.addAttribute("slots", timetableService.getDailySchedule(day));
        model.addAttribute("day", day);
        return "timetable/day-view";
    }

    @PostMapping("/generate")
    public String generateTimetable(RedirectAttributes redirectAttributes) {
        DayOfWeek movieNightDay = timetableService.generateTimetable();
        redirectAttributes.addFlashAttribute("success",
                "Timetable generated. Movie night this week: " + movieNightDay);
        return "redirect:/timetable";
    }
}
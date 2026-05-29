package com.Sarthak.course_recommender.controller;

import com.Sarthak.course_recommender.model.enums.AttendanceStatus;
import com.Sarthak.course_recommender.service.AttendanceService;
import com.Sarthak.course_recommender.service.ClassGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final ClassGroupService classGroupService;

    @GetMapping("/class/{classGroupId}")
    public String viewAttendance(@PathVariable Long classGroupId,
                                 @RequestParam(required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 Model model) {
        if (date == null) date = LocalDate.now();

        model.addAttribute("classGroup", classGroupService.getClassGroupById(classGroupId));
        model.addAttribute("attendance", attendanceService.getAttendanceForClassOnDate(classGroupId, date));
        model.addAttribute("absenceCounts", attendanceService.getAbsenceCountByStudent(classGroupId));
        model.addAttribute("date", date);
        model.addAttribute("statuses", AttendanceStatus.values());
        return "attendance/mark";
    }

    @PostMapping("/class/{classGroupId}/mark")
    public String markAttendance(@PathVariable Long classGroupId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 @RequestParam Map<String, String> params,
                                 RedirectAttributes redirectAttributes) {
        // Filter only student attendance entries from params
        Map<Long, String> attendanceMap = params.entrySet().stream()
                .filter(e -> e.getKey().startsWith("student_"))
                .collect(java.util.stream.Collectors.toMap(
                        e -> Long.parseLong(e.getKey().replace("student_", "")),
                        Map.Entry::getValue
                ));

        attendanceService.markAttendance(classGroupId, date, attendanceMap);
        redirectAttributes.addFlashAttribute("success", "Attendance saved for " + date);
        return "redirect:/attendance/class/" + classGroupId + "?date=" + date;
    }

    @GetMapping("/student/{studentId}")
    public String viewStudentAttendance(@PathVariable Long studentId, Model model) {
        model.addAttribute("attendanceList", attendanceService.getStudentAttendance(studentId));
        model.addAttribute("studentId", studentId);
        return "attendance/student-view";
    }
}
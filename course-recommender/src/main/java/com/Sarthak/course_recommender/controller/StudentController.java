package com.Sarthak.course_recommender.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;
import com.Sarthak.course_recommender.service.DormService;
import com.Sarthak.course_recommender.service.StudentService;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final DormService dormService;

    @GetMapping
    public String listStudents(@RequestParam(required = false) String gender,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String name,
            Model model) {

        List<Student> students = studentService.getAllStudents();

        if (gender != null && !gender.isEmpty()) {
            Gender g = Gender.valueOf(gender);
            students = students.stream()
                    .filter(s -> s.getGender() == g)
                    .collect(java.util.stream.Collectors.toList());
        }

        if (section != null && !section.isEmpty()) {
            Section sec = Section.valueOf(section);
            students = students.stream()
                    .filter(s -> s.getSection() == sec)
                    .collect(java.util.stream.Collectors.toList());
        }

        if (stream != null && !stream.isEmpty()) {
            SubjectGroup sg = SubjectGroup.valueOf(stream);
            students = students.stream()
                    .filter(s -> s.getSubjectGroup() == sg)
                    .collect(java.util.stream.Collectors.toList());
        }

        // Name search — case insensitive, partial match
        if (name != null && !name.isEmpty()) {
            String lower = name.toLowerCase();
            students = students.stream()
                    .filter(s -> s.getName().toLowerCase().contains(lower))
                    .collect(java.util.stream.Collectors.toList());
        }

        model.addAttribute("students", students);
        model.addAttribute("totalCount", studentService.getTotalStudentCount());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("sections", Section.values());
        model.addAttribute("streams", SubjectGroup.values());
        model.addAttribute("selectedGender", gender);
        model.addAttribute("selectedSection", section);
        model.addAttribute("selectedStream", stream);
        model.addAttribute("selectedName", name);
        return "students/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("sections", Section.values());
        return "students/form";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute Student student,
            RedirectAttributes redirectAttributes) {
        studentService.addStudent(student);
        redirectAttributes.addFlashAttribute("success", "Student added successfully.");
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/detail";
    }

    @GetMapping("/{id}/assign-dorm")
    public String showAssignDormForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("availableDorms",
                dormService.getAvailableDormsWithCount(student.getGender()));
        return "students/assign-dorm";
    }

    @PostMapping("/{id}/assign-dorm")
    public String assignDorm(@PathVariable Long id,
            @RequestParam Long dormId,
            RedirectAttributes redirectAttributes) {
        studentService.assignStudentToDorm(id, dormId);
        redirectAttributes.addFlashAttribute("success", "Student assigned to dorm.");
        return "redirect:/students/" + id;
    }

    @PostMapping("/{id}/remove-dorm")
    public String removeDorm(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        studentService.removeStudentFromDorm(id);
        redirectAttributes.addFlashAttribute("success", "Student removed from dorm.");
        return "redirect:/students/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        studentService.removeStudent(id);
        redirectAttributes.addFlashAttribute("success", "Student removed.");
        return "redirect:/students";
    }

    @PostMapping("/assign-all-dorms")
    public String assignAllDorms(RedirectAttributes redirectAttributes) {
        studentService.assignAllUnassignedToDorms();
        redirectAttributes.addFlashAttribute("success",
                "All unassigned students have been assigned to dorms.");
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        model.addAttribute("genders", Gender.values());
        model.addAttribute("sections", Section.values());
        return "students/edit";
    }

    @PostMapping("/{id}/edit")
    public String editStudent(@PathVariable Long id,
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes) {
        studentService.updateStudent(id, student);
        redirectAttributes.addFlashAttribute("success", "Student updated successfully.");
        return "redirect:/students/" + id;
    }

    @GetMapping("/{id}/transfer-dorm")
    public String showTransferDormForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("availableDorms",
                dormService.getAvailableDormsWithCount(student.getGender()));
        return "students/transfer-dorm";
    }

    @GetMapping("/{id}/id-card")
    public String viewIdCard(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/id-card";
    }
}
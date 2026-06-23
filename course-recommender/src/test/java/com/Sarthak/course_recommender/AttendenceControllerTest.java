package com.Sarthak.course_recommender.controller;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;
import com.Sarthak.course_recommender.service.AttendanceService;
import com.Sarthak.course_recommender.service.ClassGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private ClassGroupService classGroupService;

    @InjectMocks
    private AttendanceController attendanceController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // No-op view resolver so Thymeleaf templates are not rendered during tests
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController)
                .setViewResolvers((viewName, locale) -> (model, request, response) -> { })
                .build();
    }

    @Test
    void shouldReturnAttendanceMarkView() throws Exception {
        Long classGroupId = 1L;
        LocalDate date = LocalDate.now();

        ClassGroup classGroup = ClassGroup.builder()
                .groupName("CPE-1")
                .subjectGroup(SubjectGroup.COMPUTER_PHYSICS)
                .build();

        when(classGroupService.getClassGroupById(classGroupId)).thenReturn(classGroup);
        when(attendanceService.getAttendanceForClassOnDate(classGroupId, date)).thenReturn(Collections.emptyList());
        when(attendanceService.getAbsenceCountByStudent(classGroupId)).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/attendance/class/{classGroupId}", classGroupId)
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("attendance/mark"))
                .andExpect(model().attributeExists("classGroup", "attendance", "absenceCounts", "date", "statuses"));

        verify(classGroupService).getClassGroupById(classGroupId);
        verify(attendanceService).getAttendanceForClassOnDate(classGroupId, date);
        verify(attendanceService).getAbsenceCountByStudent(classGroupId);
    }

    @Test
    void shouldMarkAttendanceAndRedirect() throws Exception {
        Long classGroupId = 1L;
        LocalDate date = LocalDate.now();

        mockMvc.perform(post("/attendance/class/{classGroupId}/mark", classGroupId)
                        .param("date", date.toString())
                        .param("student_1", "PRESENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/attendance/class/" + classGroupId + "?date=" + date));

        verify(attendanceService).markAttendance(eq(classGroupId), eq(date), any());
    }

    @Test
    void shouldReturnStudentAttendanceView() throws Exception {
        Long studentId = 1L;

  

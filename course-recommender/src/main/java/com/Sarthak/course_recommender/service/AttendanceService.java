package com.Sarthak.course_recommender.service;

import com.Sarthak.course_recommender.model.Attendance;
import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.AttendanceStatus;
import com.Sarthak.course_recommender.repository.AttendanceRepository;
import com.Sarthak.course_recommender.repository.ClassGroupRepository;
import com.Sarthak.course_recommender.repository.StudentRepository;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassGroupRepository classGroupRepository;
    private final StudentRepository studentRepository;

    // ── Read ────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Attendance> getAttendanceForClassOnDate(Long classGroupId, LocalDate date) {
        ClassGroup group = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassGroup not found: " + classGroupId));
        return attendanceRepository.findByClassGroupAndDate(group, date);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getStudentAttendance(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return attendanceRepository.findByStudent(student);
    }

    // ── Mark attendance ─────────────────────────────────────

    /**
     * Takes a map of studentId -> status string ("PRESENT" or "ABSENT")
     * and saves attendance for the whole class on a given date.
     * If a record already exists for that student+date it updates it.
     */
    @Transactional
    public void markAttendance(Long classGroupId, LocalDate date,
                               Map<Long, String> attendanceMap) {
        ClassGroup group = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassGroup not found: " + classGroupId));

        for (Map.Entry<Long, String> entry : attendanceMap.entrySet()) {
            Student student = studentRepository.findById(entry.getKey())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + entry.getKey()));

            AttendanceStatus status = AttendanceStatus.valueOf(entry.getValue());

            Attendance attendance = attendanceRepository
                    .findByStudentAndDate(student, date)
                    .orElse(Attendance.builder()
                            .student(student)
                            .classGroup(group)
                            .date(date)
                            .build());

            attendance.setStatus(status);
            attendanceRepository.save(attendance);
        }
    }

    // ── Summary ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public Map<Long, Long> getAbsenceCountByStudent(Long classGroupId) {
        ClassGroup group = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassGroup not found: " + classGroupId));

        return attendanceRepository
                .countByStatusAndClassGroup(AttendanceStatus.ABSENT, group)
                .stream()
                .collect(Collectors.toMap(
                        row -> ((Student) row[0]).getId(),
                        row -> (Long) row[1]
                ));
    }
}
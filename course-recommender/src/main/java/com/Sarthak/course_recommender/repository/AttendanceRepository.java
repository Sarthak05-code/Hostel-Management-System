package com.Sarthak.course_recommender.repository;

import com.Sarthak.course_recommender.model.Attendance;
import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByClassGroupAndDate(ClassGroup classGroup, LocalDate date);

    List<Attendance> findByStudent(Student student);

    Optional<Attendance> findByStudentAndDate(Student student, LocalDate date);

    // Count absences per student — used for flagging frequent absentees
    @Query("SELECT a.student, COUNT(a) FROM Attendance a WHERE a.status = :status AND a.classGroup = :classGroup GROUP BY a.student")
    List<Object[]> countByStatusAndClassGroup(@Param("status") AttendanceStatus status,
                                              @Param("classGroup") ClassGroup classGroup);
}
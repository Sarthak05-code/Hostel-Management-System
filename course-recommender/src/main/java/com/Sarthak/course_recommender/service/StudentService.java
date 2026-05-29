package com.Sarthak.course_recommender.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;
import com.Sarthak.course_recommender.repository.ClassGroupRepository;
import com.Sarthak.course_recommender.repository.DormRepository;
import com.Sarthak.course_recommender.repository.StudentRepository;
import com.Sarthak.course_recommender.service.exception.DormFullException;
import com.Sarthak.course_recommender.service.exception.GenderMismatchException;
import com.Sarthak.course_recommender.service.exception.HostelCapacityExceededException;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final int MAX_HOSTEL_CAPACITY = 200;

    private final StudentRepository studentRepository;
    private final DormRepository dormRepository;
    private final ClassGroupRepository classGroupRepository;

    // ── Read ────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public List<Student> getStudentsByGender(Gender gender) {
        return studentRepository.findByGender(gender);
    }

    public List<Student> getStudentsBySection(Section section) {
        return studentRepository.findBySection(section);
    }

    @Transactional(readOnly = true)
    public List<Student> getStudentsByDorm(Long dormId) {
        Dorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new ResourceNotFoundException("Dorm not found with id: " + dormId));
        return studentRepository.findByDorm(dorm);
    }

    public List<Student> getUnassignedStudents() {
        return studentRepository.findByClassGroupIsNull();
    }

    public long getTotalStudentCount() {
        return studentRepository.countBy();
    }

    // ── Create ──────────────────────────────────────────────

    @Transactional
    public Student addStudent(Student student) {
        if (studentRepository.countBy() >= MAX_HOSTEL_CAPACITY) {
            throw new HostelCapacityExceededException();
        }
        return studentRepository.save(student);
    }

    // ── Dorm Assignment ─────────────────────────────────────

    @Transactional
    public Student assignStudentToDorm(Long studentId, Long dormId) {
        Student student = getStudentById(studentId);
        Dorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new ResourceNotFoundException("Dorm not found with id: " + dormId));

        if (dorm.getGender() != student.getGender()) {
            throw new GenderMismatchException(
                    "Student gender " + student.getGender() +
                            " does not match dorm gender " + dorm.getGender());
        }

        if (studentRepository.countByDorm(dorm) >= Dorm.MAX_CAPACITY) {
            throw new DormFullException(dorm.getName());
        }

        student.setDorm(dorm);
        return studentRepository.save(student);
    }

    @Transactional
    public Student removeStudentFromDorm(Long studentId) {
        Student student = getStudentById(studentId);
        student.setDorm(null);
        return studentRepository.save(student);
    }

    // ── Delete ──────────────────────────────────────────────

    @Transactional
    public void removeStudent(Long studentId) {
        Student student = getStudentById(studentId);
        studentRepository.delete(student);
    }

    @Transactional
    public void assignAllUnassignedToDorms() {
        List<Student> unassigned = studentRepository.findByDormIsNull();
        Random random = new Random();

        for (Student student : unassigned) {
            List<Dorm> available = dormRepository
                    .findAvailableDormsByGender(student.getGender(), Dorm.MAX_CAPACITY);
            if (available.isEmpty())
                continue;
            student.setDorm(available.get(random.nextInt(available.size())));
            studentRepository.save(student);
        }
    }

    // @Transactional
    // public Student updateStudent(Long id, Student updated) {
    // Student existing = getStudentById(id);
    // existing.setName(updated.getName());
    // existing.setGender(updated.getGender());
    // existing.setSection(updated.getSection());
    // return studentRepository.save(existing);
    // }

    @Transactional
    public Student updateStudent(Long id, Student updated) {
        Student existing = getStudentById(id);

        boolean streamChanged = SubjectGroup.fromSection(existing.getSection()) != SubjectGroup
                .fromSection(updated.getSection());

        existing.setName(updated.getName());
        existing.setGender(updated.getGender());
        existing.setSection(updated.getSection());

        // Only reassign class group if stream actually changed
        if (streamChanged) {
            SubjectGroup group = SubjectGroup.fromSection(updated.getSection());
            List<ClassGroup> classGroups = classGroupRepository.findBySubjectGroup(group);

            if (!classGroups.isEmpty()) {
                ClassGroup selected = classGroups.stream()
                        .min((a, b) -> a.getStudents().size() - b.getStudents().size())
                        .orElse(classGroups.get(0));
                existing.setClassGroup(selected);
            } else {
                existing.setClassGroup(null);
            }
        }

        return studentRepository.save(existing);
    }

  
}
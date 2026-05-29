package com.Sarthak.course_recommender.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;
import com.Sarthak.course_recommender.model.enums.SubjectGroup;
import com.Sarthak.course_recommender.repository.ClassGroupRepository;
import com.Sarthak.course_recommender.repository.StudentRepository;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassGroupService {

    private static final int TARGET_CLASS_SIZE = 20;

    private final ClassGroupRepository classGroupRepository;
    private final StudentRepository studentRepository;

    // ── Read ────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ClassGroup> getAllClassGroups() {
        return classGroupRepository.findAll();
    }

    public ClassGroup getClassGroupById(Long id) {
        return classGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassGroup not found with id: " + id));
    }

    public List<ClassGroup> getClassGroupsByStream(SubjectGroup subjectGroup) {
        return classGroupRepository.findBySubjectGroup(subjectGroup);
    }

    public List<Object[]> getClassSizeSummary() {
        return classGroupRepository.countStudentsPerGroup();
    }

    // ── Class Generation ────────────────────────────────────

    /**
     * Core algorithm. Wipes existing class groups and regenerates from scratch.
     *
     * Steps:
     *  1. Null out all students' classGroup references
     *  2. Delete all existing ClassGroups
     *  3. For each stream (CP and AE):
     *     a. Fetch all students in that stream
     *     b. Calculate optimal number of classes
     *     c. Shuffle males and females separately
     *     d. Assign round-robin to ensure gender balance per class
     */
    @Transactional
    public void generateClasses() {
        // Step 1 — clear existing class assignments from students
        studentRepository.findAll().forEach(s -> {
            s.setClassGroup(null);
            studentRepository.save(s);
        });

        // Step 2 — delete all class groups
        classGroupRepository.deleteAll();

        // Step 3 — generate for each stream
        for (SubjectGroup stream : SubjectGroup.values()) {
            generateForStream(stream);
        }
    }

    private void generateForStream(SubjectGroup stream) {
        List<Section> sections = stream.getSections();
        List<Student> allStudents = studentRepository.findBySections(sections);

        if (allStudents.isEmpty()) return;

        int k = calculateOptimalClassCount(allStudents.size());
        String prefix = stream == SubjectGroup.COMPUTER_PHYSICS ? "CPE" : "AE";

        // Create k ClassGroup entities
        List<ClassGroup> groups = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            ClassGroup group = ClassGroup.builder()
                    .groupName(prefix + "-" + i)
                    .subjectGroup(stream)
                    .build();
            groups.add(classGroupRepository.save(group));
        }

        // Split by gender and shuffle each independently
        List<Student> males = allStudents.stream()
                .filter(s -> s.getGender() == Gender.MALE)
                .collect(java.util.stream.Collectors.toList());

        List<Student> females = allStudents.stream()
                .filter(s -> s.getGender() == Gender.FEMALE)
                .collect(java.util.stream.Collectors.toList());

        Collections.shuffle(males);
        Collections.shuffle(females);

        // Assign males round-robin across groups
        for (int i = 0; i < males.size(); i++) {
            Student s = males.get(i);
            s.setClassGroup(groups.get(i % k));
            studentRepository.save(s);
        }

        // Assign females round-robin across groups
        for (int i = 0; i < females.size(); i++) {
            Student s = females.get(i);
            s.setClassGroup(groups.get(i % k));
            studentRepository.save(s);
        }
    }

    /**
     * Finds the number of classes k such that students are
     * distributed as evenly as possible with a target size of 20.
     *
     * Examples:
     *   140 students → k=7 → 7 classes of 20
     *   145 students → k=7 → 5 classes of 21, 2 classes of 20
     *   30  students → k=2 → 1 class of 15, 1 class of 15
     *   10  students → k=1 → 1 class of 10
     */
    private int calculateOptimalClassCount(int studentCount) {
        return Math.max(1, (int) Math.round((double) studentCount / TARGET_CLASS_SIZE));
    }

    // ── Delete ──────────────────────────────────────────────

    @Transactional
    public void deleteClassGroup(Long id) {
        ClassGroup group = getClassGroupById(id);
        // Unassign all students first
        studentRepository.findByClassGroup(group).forEach(s -> {
            s.setClassGroup(null);
            studentRepository.save(s);
        });
        classGroupRepository.delete(group);
    }

    public void assignStudentToCorrectClass(Student student) {
    SubjectGroup stream = SubjectGroup.fromSection(student.getSection());

    List<ClassGroup> groups = classGroupRepository.findBySubjectGroup(stream);

    if (groups.isEmpty()) return;

    ClassGroup selected = groups.get(0); // or better logic

    student.setClassGroup(selected);
}

    public List<ClassGroup> findBySubjectGroup(SubjectGroup group) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findBySubjectGroup'");
    }
}
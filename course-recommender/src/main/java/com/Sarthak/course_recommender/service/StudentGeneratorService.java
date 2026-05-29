package com.Sarthak.course_recommender.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.Student;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.model.enums.Section;
import com.Sarthak.course_recommender.repository.DormRepository;
import com.Sarthak.course_recommender.repository.StudentRepository;
import com.Sarthak.course_recommender.service.exception.HostelCapacityExceededException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class StudentGeneratorService {

    private static final int MAX_HOSTEL_CAPACITY = 200;

    private final StudentRepository studentRepository;
    private final DormRepository dormRepository;

    // ── Name pools ──────────────────────────────────────────

    private static final List<String> MALE_NAMES = List.of(
            "Aarav", "Bikash", "Chirag", "Deepak", "Eshan",
            "Firoz", "Gaurav", "Hari", "Ishan", "Jeevan",
            "Kiran", "Lokesh", "Manish", "Nabin", "Omkar",
            "Pratik", "Rahul", "Sagar", "Tushar", "Umesh",
            "Vivek", "Yash", "Ankit", "Binod", "Chetan",
            "Dinesh", "Gopal", "Hemant", "Kamal", "Niraj"
    );

    private static final List<String> FEMALE_NAMES = List.of(
            "Anita", "Bina", "Chanda", "Divya", "Ekta",
            "Fatima", "Gita", "Hina", "Isha", "Jyoti",
            "Kavya", "Laxmi", "Manisha", "Nisha", "Puja",
            "Radha", "Sarita", "Tara", "Uma", "Vineeta",
            "Yana", "Zara", "Alisha", "Bishnu", "Chameli",
            "Deepa", "Geeta", "Kamala", "Menuka", "Sunita"
    );

    private static final List<String> LAST_NAMES = List.of(
            "Sharma", "Thapa", "Shrestha", "Adhikari", "Poudel",
            "Karki", "Rai", "Gurung", "Tamang", "Magar",
            "Basnet", "Pandey", "Koirala", "Bhattarai", "Regmi",
            "Khatri", "Limbu", "Subedi", "Oli", "Chaudhary"
    );

    // ── Generator ───────────────────────────────────────────

    @Transactional
    public int generateStudents(int count) {
        long current = studentRepository.countBy();

        if (current >= MAX_HOSTEL_CAPACITY) {
            throw new HostelCapacityExceededException();
        }

        // Clamp to remaining capacity
        int canAdd = (int) Math.min(count, MAX_HOSTEL_CAPACITY - current);

        Random random = new Random();
        Section[] sections = Section.values();

        // Split evenly by gender — half male, half female
        int maleCount = canAdd / 2;
        int femaleCount = canAdd - maleCount;

        List<Student> students = new ArrayList<>();

        for (int i = 0; i < maleCount; i++) {
            students.add(buildStudent(Gender.MALE, sections, random));
        }
        for (int i = 0; i < femaleCount; i++) {
            students.add(buildStudent(Gender.FEMALE, sections, random));
        }

        // Shuffle so male/female aren't added in a block
        Collections.shuffle(students);

        for (Student student : students) {
            Student saved = studentRepository.save(student);
            assignToDorm(saved, random);
        }

        return canAdd;
    }

    private Student buildStudent(Gender gender, Section[] sections, Random random) {
        List<String> firstNames = gender == Gender.MALE ? MALE_NAMES : FEMALE_NAMES;
        String firstName = firstNames.get(random.nextInt(firstNames.size()));
        String lastName = LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
        Section section = sections[random.nextInt(sections.length)];

        return Student.builder()
                .name(firstName + " " + lastName)
                .gender(gender)
                .section(section)
                .build();
    }

    private void assignToDorm(Student student, Random random) {
        List<Dorm> availableDorms = dormRepository
                .findAvailableDormsByGender(student.getGender(), Dorm.MAX_CAPACITY);

        if (availableDorms.isEmpty()) return; // no dorm available, student stays unassigned

        Dorm dorm = availableDorms.get(random.nextInt(availableDorms.size()));
        student.setDorm(dorm);
        studentRepository.save(student);
    }
}
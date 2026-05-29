package com.Sarthak.course_recommender.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.dto.DormAvailabilityDto;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.repository.DormRepository;
import com.Sarthak.course_recommender.repository.StudentRepository;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DormService {

    private final DormRepository dormRepository;
    private final StudentRepository studentRepository;

    // ── Read ────────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<Dorm> getAllDorms() {
        return dormRepository.findAllWithSupervisor();
    }

    @Transactional(readOnly = true)
    public Dorm getDormById(Long id) {
        return dormRepository.findByIdWithSupervisor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dorm not found with id: " + id));
    }

    public List<Dorm> getDormsByGender(Gender gender) {
        return dormRepository.findByGender(gender);
    }

    public List<Dorm> getAvailableDorms(Gender gender) {
        return dormRepository.findAvailableDormsByGender(gender, Dorm.MAX_CAPACITY);
    }

    public List<Dorm> getOverCapacityDorms() {
        return dormRepository.findOverCapacityDorms(Dorm.MAX_CAPACITY);
    }

    public long getOccupancy(Long dormId) {
        Dorm dorm = getDormById(dormId);
        return studentRepository.countByDorm(dorm);
    }

    // ── Create / Update ─────────────────────────────────────

    @Transactional
    public Dorm createDorm(Dorm dorm) {
        return dormRepository.save(dorm);
    }

    @Transactional
    public Dorm updateDormName(Long dormId, String newName) {
        Dorm dorm = getDormById(dormId);
        dorm.setName(newName);
        return dormRepository.save(dorm);
    }

    // ── Delete ──────────────────────────────────────────────

    @Transactional
    public void deleteDorm(Long dormId) {
        Dorm dorm = getDormById(dormId);
        // Unassign all students in this dorm before deleting
        studentRepository.findByDorm(dorm)
                .forEach(s -> {
                    s.setDorm(null);
                    studentRepository.save(s);
                });
        dormRepository.delete(dorm);
    }

    @Transactional(readOnly = true)
    public List<DormAvailabilityDto> getAvailableDormsWithCount(Gender gender) {
        List<Dorm> dorms = dormRepository.findAvailableDormsByGender(gender, Dorm.MAX_CAPACITY);
        return dorms.stream()
                .map(d -> new DormAvailabilityDto(d, studentRepository.countByDorm(d)))
                .collect(java.util.stream.Collectors.toList());
    }
}

package com.Sarthak.course_recommender.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Sarthak.course_recommender.model.Dorm;
import com.Sarthak.course_recommender.model.Supervisor;
import com.Sarthak.course_recommender.model.enums.Gender;
import com.Sarthak.course_recommender.repository.DormRepository;
import com.Sarthak.course_recommender.repository.SupervisorRepository;
import com.Sarthak.course_recommender.service.exception.GenderMismatchException;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupervisorService {

    private final SupervisorRepository supervisorRepository;
    private final DormRepository dormRepository;

    // ── Read ────────────────────────────────────────────────

    public List<Supervisor> getAllSupervisors() {
        return supervisorRepository.findAll();
    }

    public Supervisor getSupervisorById(Long id) {
        return supervisorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supervisor not found with id: " + id));
    }

    public List<Supervisor> getUnassignedSupervisors() {
        return supervisorRepository.findByDormIsNull();
    }

    public List<Supervisor> getSupervisorsByGender(Gender gender) {
        return supervisorRepository.findByGender(gender);
    }

    // ── Create ──────────────────────────────────────────────

    @Transactional
    public Supervisor addSupervisor(Supervisor supervisor) {
        return supervisorRepository.save(supervisor);
    }

    // ── Dorm Assignment ─────────────────────────────────────

    @Transactional
    public Supervisor assignSupervisorToDorm(Long supervisorId, Long dormId) {
        Supervisor supervisor = getSupervisorById(supervisorId);
        Dorm dorm = dormRepository.findById(dormId)
                .orElseThrow(() -> new ResourceNotFoundException("Dorm not found with id: " + dormId));

        if (dorm.getGender() != supervisor.getGender()) {
            throw new GenderMismatchException(
                    "Supervisor gender " + supervisor.getGender() +
                    " does not match dorm gender " + dorm.getGender());
        }

        // Unassign previous supervisor of this dorm if any
        supervisorRepository.findByDorm(dorm)
                .ifPresent(existing -> {
                    existing.setDorm(null);
                    supervisorRepository.save(existing);
                });

        supervisor.setDorm(dorm);
        return supervisorRepository.save(supervisor);
    }

    @Transactional
    public Supervisor removeSupervisorFromDorm(Long supervisorId) {
        Supervisor supervisor = getSupervisorById(supervisorId);
        supervisor.setDorm(null);
        return supervisorRepository.save(supervisor);
    }

    // ── Delete ──────────────────────────────────────────────

    @Transactional
    public void removeSupervisor(Long supervisorId) {
        Supervisor supervisor = getSupervisorById(supervisorId);
        supervisorRepository.delete(supervisor);
    }
}
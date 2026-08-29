package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.Enrollment;
import com.capacityconnect.backend.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment enroll(Long userId, Long courseId) {
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(userId);
        enrollment.setCourseId(courseId);
        enrollment.setStatus("ENROLLED");
        enrollment.setProgressPercent(0);
        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsForUser(Long userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    public Enrollment updateProgress(Long enrollmentId, Integer progressPercent) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setProgressPercent(progressPercent);

        if (progressPercent >= 100) {
            enrollment.setStatus("COMPLETED");
        }

        return enrollmentRepository.save(enrollment);
    }
}

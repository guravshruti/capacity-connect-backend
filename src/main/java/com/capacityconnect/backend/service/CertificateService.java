package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.Certificate;
import com.capacityconnect.backend.model.Course;
import com.capacityconnect.backend.model.Enrollment;
import com.capacityconnect.backend.model.User;
import com.capacityconnect.backend.repository.CertificateRepository;
import com.capacityconnect.backend.repository.CourseRepository;
import com.capacityconnect.backend.repository.EnrollmentRepository;
import com.capacityconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Certificate generateCertificate(Long enrollmentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findById(enrollmentId);

        if (enrollmentOpt.isEmpty()) {
            throw new RuntimeException("Enrollment not found");
        }

        Enrollment enrollment = enrollmentOpt.get();

        if (!"COMPLETED".equals(enrollment.getStatus())) {
            throw new RuntimeException("Course is not completed yet. Certificate cannot be issued.");
        }

        Long userId = enrollment.getUserId();
        Long courseId = enrollment.getCourseId();

        Optional<Certificate> existing = certificateRepository.findByUserIdAndCourseId(userId, courseId);
        if (existing.isPresent()) {
            return existing.get();
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        String userName = userOpt.isPresent() ? userOpt.get().getName() : "Unknown User";
        String courseTitle = courseOpt.isPresent() ? courseOpt.get().getTitle() : "Unknown Course";

        String certificateCode = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Certificate certificate = new Certificate(
                userId,
                courseId,
                userName,
                courseTitle,
                certificateCode,
                LocalDate.now()
        );

        return certificateRepository.save(certificate);
    }

    public List<Certificate> getCertificatesForUser(Long userId) {
        return certificateRepository.findByUserId(userId);
    }
}

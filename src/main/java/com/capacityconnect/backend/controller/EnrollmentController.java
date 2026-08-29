package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Enrollment;
import com.capacityconnect.backend.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public Enrollment enroll(@RequestParam Long userId, @RequestParam Long courseId) {
        return enrollmentService.enroll(userId, courseId);
    }

    @GetMapping("/user/{userId}")
    public List<Enrollment> getEnrollmentsForUser(@PathVariable Long userId) {
        return enrollmentService.getEnrollmentsForUser(userId);
    }

    @PutMapping("/{enrollmentId}/progress")
    public Enrollment updateProgress(@PathVariable Long enrollmentId, @RequestParam Integer progressPercent) {
        return enrollmentService.updateProgress(enrollmentId, progressPercent);
    }
}
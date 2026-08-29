package com.capacityconnect.backend.service;

import com.capacityconnect.backend.repository.UserRepository;
import com.capacityconnect.backend.repository.CourseRepository;
import com.capacityconnect.backend.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepository.count());
        stats.put("totalCourses", courseRepository.count());
        stats.put("totalEnrollments", enrollmentRepository.count());

        long completed = enrollmentRepository.findAll().stream()
                .filter(e -> "COMPLETED".equals(e.getStatus()))
                .count();

        stats.put("completedEnrollments", completed);

        return stats;
    }
}

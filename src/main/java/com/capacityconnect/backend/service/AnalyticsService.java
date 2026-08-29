package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.AnalyticsSummary;
import com.capacityconnect.backend.model.Course;
import com.capacityconnect.backend.model.Enrollment;
import com.capacityconnect.backend.repository.CourseRepository;
import com.capacityconnect.backend.repository.EnrollmentRepository;
import com.capacityconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnalyticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public AnalyticsSummary getSummary() {
        long totalUsers = userRepository.count();
        long totalCourses = courseRepository.count();

        List<Enrollment> allEnrollments = enrollmentRepository.findAll();

        Map<Long, Long> countByCourseId = new HashMap<>();
        for (Enrollment enrollment : allEnrollments) {
            Long courseId = enrollment.getCourseId();
            countByCourseId.put(courseId, countByCourseId.getOrDefault(courseId, 0L) + 1);
        }

        String mostPopularCourseName = "No enrollments yet";
        long highestCount = 0;

        for (Map.Entry<Long, Long> entry : countByCourseId.entrySet()) {
            if (entry.getValue() > highestCount) {
                highestCount = entry.getValue();
                Optional<Course> course = courseRepository.findById(entry.getKey());
                if (course.isPresent()) {
                    mostPopularCourseName = course.get().getTitle();
                }
            }
        }

        return new AnalyticsSummary(totalUsers, totalCourses, mostPopularCourseName);
    }
}
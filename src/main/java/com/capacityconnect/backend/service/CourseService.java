package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.Course;
import com.capacityconnect.backend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course addCourse(Course course) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Course title is required");
        }
        if (course.getDescription() == null || course.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Course description is required");
        }
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(updatedCourse.getTitle());
        course.setDescription(updatedCourse.getDescription());

        // NEW FIELDS — without these lines, updates via PUT would silently
        // wipe/ignore category, level, duration, image, status, completedYear
        course.setCategory(updatedCourse.getCategory());
        course.setLevel(updatedCourse.getLevel());
        course.setDurationHours(updatedCourse.getDurationHours());
        course.setImageUrl(updatedCourse.getImageUrl());
        course.setStatus(updatedCourse.getStatus());
        course.setCompletedYear(updatedCourse.getCompletedYear());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
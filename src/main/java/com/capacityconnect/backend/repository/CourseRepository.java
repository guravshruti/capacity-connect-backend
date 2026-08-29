package com.capacityconnect.backend.repository;

import com.capacityconnect.backend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
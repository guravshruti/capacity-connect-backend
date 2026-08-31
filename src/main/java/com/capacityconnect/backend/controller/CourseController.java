package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Course;
import com.capacityconnect.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @CrossOrigin(origins="*")
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course, @RequestParam String role) {
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can add courses");
        }
        return courseService.addCourse(course);
    }

    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse, @RequestParam String role) {
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can update courses");
        }
        return courseService.updateCourse(id, updatedCourse);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id, @RequestParam String role) {
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can delete courses");
        }
        courseService.deleteCourse(id);
        return "Course deleted successfully";
    }
    @CrossOrigin(origins="*")
    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam String keyword) {
        return courseService.searchCourses(keyword);
    }
}
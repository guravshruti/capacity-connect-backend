package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.Course;
import com.capacityconnect.backend.model.Material;
import com.capacityconnect.backend.repository.CourseRepository;
import com.capacityconnect.backend.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Add a new material to a course
    public Material addMaterial(String title, String url, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Material material = new Material(title, url, course);
        return materialRepository.save(material);
    }

    // Get all materials for a given course
    public List<Material> getMaterialsByCourse(Long courseId) {
        return materialRepository.findByCourseId(courseId);
    }

    public void deleteMaterial(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new RuntimeException("Material not found with id: " + materialId);
        }
        materialRepository.deleteById(materialId);
    }
}
package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Material;
import com.capacityconnect.backend.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    // Trainer uploads a new material
    @PostMapping
    public Material addMaterial(@RequestBody MaterialRequest request) {
        return materialService.addMaterial(
                request.getTitle(),
                request.getUrl(),
                request.getCourseId()
        );
    }

    // Trainee (or anyone) views all materials for a course
    @GetMapping("/course/{courseId}")
    public List<Material> getMaterialsByCourse(@PathVariable Long courseId) {
        return materialService.getMaterialsByCourse(courseId);
    }
    // Add this method inside MaterialController.java, alongside addMaterial() and getMaterialsByCourse()

    @DeleteMapping("/{materialId}")
    public String deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return "Material deleted successfully";
    }

    // Small helper class just to receive the POST request's JSON body
    public static class MaterialRequest {
        private String title;
        private String url;
        private Long courseId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Long getCourseId() {
            return courseId;
        }

        public void setCourseId(Long courseId) {
            this.courseId = courseId;
        }
    }
}
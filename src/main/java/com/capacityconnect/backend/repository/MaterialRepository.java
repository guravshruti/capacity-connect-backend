package com.capacityconnect.backend.repository;

import com.capacityconnect.backend.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    // Custom method: find all materials belonging to one course
    List<Material> findByCourseId(Long courseId);
}
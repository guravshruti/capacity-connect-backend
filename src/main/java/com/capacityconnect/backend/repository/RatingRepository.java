package com.capacityconnect.backend.repository;

import com.capacityconnect.backend.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByCourseId(Long courseId);
}
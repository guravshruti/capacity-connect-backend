package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Rating;
import com.capacityconnect.backend.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    // Submit a new rating
    @PostMapping("/add")
    public Rating addRating(@RequestParam Long userId, @RequestParam Long courseId, @RequestParam Integer stars) {
        return ratingService.addRating(userId, courseId, stars);
    }

    // Get the average rating for a course
    @GetMapping("/course/{courseId}/average")
    public Double getAverageRating(@PathVariable Long courseId) {
        return ratingService.getAverageRating(courseId);
    }
}
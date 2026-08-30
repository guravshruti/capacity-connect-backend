package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.Rating;
import com.capacityconnect.backend.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    // Save a new rating (or just add another one — simplest approach for now)
    public Rating addRating(Long userId, Long courseId, Integer stars) {
        Rating rating = new Rating(userId, courseId, stars);
        return ratingRepository.save(rating);
    }

    // Calculate the average rating for one course
    public Double getAverageRating(Long courseId) {
        List<Rating> ratings = ratingRepository.findByCourseId(courseId);

        if (ratings.isEmpty()) {
            return 0.0;
        }

        int totalStars = 0;
        for (Rating rating : ratings) {
            totalStars += rating.getStars();
        }

        double average = (double) totalStars / ratings.size();
        return average;
    }
}

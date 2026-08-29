package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.Feedback;
import com.capacityconnect.backend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback addFeedback(Feedback feedback) {
        if (feedback.getRating() == null || feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackForCourse(Long courseId) {
        return feedbackRepository.findByCourseId(courseId);
    }
}
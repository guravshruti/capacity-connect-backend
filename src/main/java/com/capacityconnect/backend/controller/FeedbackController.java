package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Feedback;
import com.capacityconnect.backend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public Feedback addFeedback(@RequestBody Feedback feedback) {
        return feedbackService.addFeedback(feedback);
    }

    @GetMapping("/course/{courseId}")
    public List<Feedback> getFeedbackForCourse(@PathVariable Long courseId) {
        return feedbackService.getFeedbackForCourse(courseId);
    }
}
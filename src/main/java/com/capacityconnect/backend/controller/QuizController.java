package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.QuizQuestion;
import com.capacityconnect.backend.service.QuizService;
import com.capacityconnect.backend.repository.QuizQuestionRepository;
import com.capacityconnect.backend.model.AnswerSubmission;
import com.capacityconnect.backend.model.QuizResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;
    private final QuizQuestionRepository quizQuestionRepository;

    public QuizController(QuizService quizService, QuizQuestionRepository quizQuestionRepository) {
        this.quizService = quizService;
        this.quizQuestionRepository = quizQuestionRepository;
    }

    @PostMapping("/generate")
    public List<QuizQuestion> generateQuiz(@RequestBody QuizRequest request) {
        return quizService.generateAndSaveQuiz(
                request.getCourseId(),
                request.getTitle(),
                request.getDescription()
        );
    }

    public static class QuizRequest {
        private Long courseId;
        private String title;
        private String description;

        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    @GetMapping("/{courseId}")
    public List<QuizQuestion> getQuizForCourse(@PathVariable Long courseId) {
        return quizQuestionRepository.findByCourseId(courseId);
    }
    @PostMapping("/submit")
    public QuizResult submitQuiz(@RequestBody List<AnswerSubmission> submissions) {
        return quizService.submitQuiz(submissions);
    }
}
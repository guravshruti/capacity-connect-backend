package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.QuizQuestion;
import com.capacityconnect.backend.repository.QuizQuestionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.capacityconnect.backend.model.AnswerSubmission;
import com.capacityconnect.backend.model.QuizResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();

    private final QuizQuestionRepository quizQuestionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public QuizService(QuizQuestionRepository quizQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
    }

    public List<QuizQuestion> generateAndSaveQuiz(Long courseId, String title, String description) {

        String prompt = "You are helping create a quiz for a training course.\n\n" +
                "Course Title: " + title + "\n" +
                "Course Description: " + description + "\n\n" +
                "Generate 5 multiple-choice quiz questions based on this course.\n" +
                "Respond with ONLY a JSON array, no markdown, no code fences, no extra text.\n" +
                "Each item must have exactly these fields: \"question\", \"optionA\", \"optionB\", \"optionC\", \"optionD\", \"correctAnswer\".\n" +
                "correctAnswer must be one of: A, B, C, D.";

        String requestBody = "{ \"contents\": [ { \"parts\": [ { \"text\": "
                + toJsonString(prompt) + " } ] } ] }";

        String rawResponse = webClient.post()
                .uri("/v1beta/models/gemini-3.6-flash:generateContent?key=" + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String quizText = root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();

            quizText = quizText.replace("```json", "").replace("```", "").trim();

            JsonNode questionsArray = objectMapper.readTree(quizText);

            List<QuizQuestion> savedQuestions = new ArrayList<>();

            for (JsonNode q : questionsArray) {
                QuizQuestion question = new QuizQuestion();
                question.setCourseId(courseId);
                question.setQuestionText(q.path("question").asText());
                question.setOptionA(q.path("optionA").asText());
                question.setOptionB(q.path("optionB").asText());
                question.setOptionC(q.path("optionC").asText());
                question.setOptionD(q.path("optionD").asText());
                question.setCorrectAnswer(q.path("correctAnswer").asText());

                savedQuestions.add(quizQuestionRepository.save(question));
            }

            return savedQuestions;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse or save quiz questions: " + e.getMessage(), e);
        }
    }

    private String toJsonString(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
    public QuizResult submitQuiz(List<AnswerSubmission> submissions) {
        int correctCount = 0;

        for (AnswerSubmission submission : submissions) {
            QuizQuestion question = quizQuestionRepository.findById(submission.getQuestionId())
                    .orElse(null);

            if (question != null &&
                    question.getCorrectAnswer().equalsIgnoreCase(submission.getSelectedAnswer())) {
                correctCount++;
            }
        }

        return new QuizResult(submissions.size(), correctCount);
    }
}
package com.capacityconnect.backend.repository;

import com.capacityconnect.backend.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByCourseId(Long courseId);
}

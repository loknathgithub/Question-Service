package com.loknath.question_services.dao;

import java.util.List;

import com.loknath.question_services.models.QuizName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loknath.question_services.models.Question;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer>{  
    List<Question> findByCategory(String category);

    @Query(value = "SELECT q.id FROM Question q WHERE q.category=:categoryName ORDER BY RAND() LIMIT :numQ")
    List<Integer> findRandomQuestionByCategory(String categoryName, Integer numQ);

    List<Question> findByQuizName(QuizName quizName);

}
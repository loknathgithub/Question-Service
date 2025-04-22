package com.loknath.question_services.dao;

import com.loknath.question_services.models.QuizName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<QuizName, Integer> {
    Optional<QuizName> findByQuizsName(String quizsName);
}
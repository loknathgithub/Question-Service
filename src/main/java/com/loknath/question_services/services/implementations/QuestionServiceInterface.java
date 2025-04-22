package com.loknath.question_services.services.implementations;

import java.util.List;

import com.loknath.question_services.models.Question;
import com.loknath.question_services.models.QuestionWrapper;
import com.loknath.question_services.models.Response;

public interface QuestionServiceInterface {

    public List<Question> getallquestions();
    public Object getQuestionsByCategory(String category);
    public Object addQuestion(Question question);
    public Object findRandomQuestionByCategory(String categoryName, Integer numQ);
    public List<QuestionWrapper> getQuestionsFromId(List<Integer> questionIds);
    public Object getScore(List<Response> response);
}

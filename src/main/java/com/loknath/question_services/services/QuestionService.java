package com.loknath.question_services.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loknath.question_services.dao.QuestionRepo;
import com.loknath.question_services.models.Question;
import com.loknath.question_services.models.QuestionWrapper;
import com.loknath.question_services.models.Response;
import com.loknath.question_services.services.implementations.QuestionServiceInterface;

@Service
public class QuestionService implements QuestionServiceInterface{

    @Autowired
    private QuestionRepo repo;

    @Override
    public List<Question> getallquestions(){
        return repo.findAll();
    }

    @Override
    public Object getQuestionsByCategory(String category) {
        return repo.findByCategory(category);
    }

    @Override
    public Object addQuestion(Question question) {
        return repo.save(question);
    }

    @Override
    public Object findRandomQuestionByCategory(String categoryName, Integer numQ) {
        return repo.findRandomQuestionByCategory(categoryName, numQ);
    }

    @Override
    public List<QuestionWrapper> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for(Integer id: questionIds){
            questions.add(repo.findById(id).get());
        }

        for(Question q: questions){
            QuestionWrapper wrapper = new QuestionWrapper();

            wrapper.setId(q.getId());
            wrapper.setQuestionTitle(q.getQuestionTitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    public Object getScore(List<Response> response) {
        int right = 0;

        for(Response r : response){
            Question question = repo.findById(r.getId()).get();
            if (r.getResponse().equals(question.getRightAnswer()))
                right++;
        }
        return right;
    }

}
package com.loknath.question_services.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.loknath.question_services.models.Question;
import com.loknath.question_services.models.Response;
import com.loknath.question_services.services.QuestionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "/question", method=RequestMethod.GET)
public class QuestionController {

    @Autowired
    private QuestionService service;
    @Autowired
    private Environment environment;
    
    @GetMapping("/allquestions")
    public ResponseEntity<?> allQuestions() {
        try {
            return new ResponseEntity<>(service.getallquestions(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("/allquestions/{category}")
    public ResponseEntity<?> getQuestionsByCategory(@PathVariable String category) {
        try {
            return new ResponseEntity<>(service.getQuestionsByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        try {
            return new ResponseEntity<>(service.addQuestion(question), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/generate")
    public ResponseEntity<?> getQuiz(@RequestParam String categoryName, @RequestParam Integer numQ) {
        try {
            System.out.println(environment.getProperty("local.server.port"));
            return new ResponseEntity<>(service.findRandomQuestionByCategory(categoryName, numQ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/getquestions")
    public ResponseEntity<Object> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
        
        try {
            System.out.println(environment.getProperty("local.server.port"));
            return new ResponseEntity<>(service.getQuestionsFromId(questionIds), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @PostMapping("/getscore")
    public ResponseEntity<Object> getScore(@RequestBody List<Response> response) {
       try {
        System.out.println(environment.getProperty("local.server.port"));

        return new ResponseEntity<>(service.getScore(response), HttpStatus.OK);
        
       } catch (Exception e) {
        return new ResponseEntity<>("Message: "+e, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    
    }
    


package com.loknath.question_services.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.loknath.question_services.dao.ClassRepository;
import com.loknath.question_services.dao.QuestionRepo;
import com.loknath.question_services.models.QuizName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.loknath.question_services.models.Question;
import com.loknath.question_services.models.Response;
import com.loknath.question_services.services.QuestionService;


@RestController
@RequestMapping("/question")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class QuestionController {

    @Autowired
    private QuestionService service;
    @Autowired
    private Environment environment;
    @Autowired
    private ClassRepository classRepository;

    //To be deleted -> replaced by service class
    @Autowired
    private QuestionRepo questionRepo;


    // Get all questions stored in database
    @GetMapping("/allquestions")
    public ResponseEntity<?> allQuestions() {
        try {
            return new ResponseEntity<>(service.getallquestions(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: " + e, HttpStatus.NOT_FOUND);
        }
    }

    //Get all questions stored in database by category
    @GetMapping("/allquestions/{category}")
    public ResponseEntity<?> getQuestionsByCategory(@PathVariable String category) {
        try {
            return new ResponseEntity<>(service.getQuestionsByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.NOT_FOUND);
        }
    }


    // Adding a new question in database
    @PostMapping("/add")
    public ResponseEntity<?> addQuestion(@RequestBody Question question, @RequestParam String quizsName) {
        System.out.println(question+"\n"+quizsName);

        try {

            QuizName quizClass = classRepository.findByQuizsName(quizsName)
                    .orElseGet(() -> classRepository.save(new QuizName(quizsName)));

            question.setQuizName(quizClass);
            return new ResponseEntity<>(service.addQuestion(question), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.NOT_FOUND);
        }
    }


    // Generate a new quiz according to a category and number of questions
    // Question will be from database
    @GetMapping("/generate")
    public ResponseEntity<?> getQuiz(@RequestParam String categoryName, @RequestParam Integer numQ) {
        try {
            System.out.println(environment.getProperty("local.server.port"));
            return new ResponseEntity<>(service.findRandomQuestionByCategory(categoryName, numQ), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all questions by ids
    @PostMapping("/getquestions")
    public ResponseEntity<Object> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
        System.out.println(questionIds);
        
        try {
//            System.out.println(environment.getProperty("local.server.port"));
            return new ResponseEntity<>(service.getQuestionsFromId(questionIds), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Message: "+e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    // Get score on submitting a quiz
    @PostMapping("/getscore")
    public ResponseEntity<Object> getScore(@RequestBody List<Response> response) {
       try {
//        System.out.println(environment.getProperty("local.server.port"));
        return new ResponseEntity<>(service.getScore(response), HttpStatus.OK);
       } catch (Exception e) {
        return new ResponseEntity<>("Message: "+e, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @GetMapping("/by-class/{quizsName}")
    public ResponseEntity<?> getQuestionsByClass(@PathVariable String quizsName) {
        System.out.println(quizsName);
        try {
            // Find the QuizName entity
            QuizName quizClass = classRepository.findByQuizsName(quizsName)
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            // Fetch questions for this class
            List<Question> questions = questionRepo.findByQuizName(quizClass);
            System.out.println(questions);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateQuestion(@PathVariable int id, @RequestBody Question updatedQuestion) {

        System.out.println(updatedQuestion+"\n"+id);
        try {
            Question existingQuestion = questionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            // Update fields
            existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
            existingQuestion.setOption1(updatedQuestion.getOption1());
            existingQuestion.setOption2(updatedQuestion.getOption2());
            existingQuestion.setOption3(updatedQuestion.getOption3());
            existingQuestion.setOption4(updatedQuestion.getOption4());
            existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
            existingQuestion.setDifficulty(updatedQuestion.getDifficulty());
            existingQuestion.setCategory(updatedQuestion.getCategory());

            questionRepo.save(existingQuestion);
            return new ResponseEntity<>(existingQuestion, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable int id) {
        try {
            Question question = questionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            questionRepo.delete(question);
            return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/by-class/{quizsName}")
    public ResponseEntity<?> deleteQuestionsByClass(@PathVariable String quizsName) {
        try {
            QuizName quizClass = classRepository.findByQuizsName(quizsName)
                    .orElseThrow(() -> new RuntimeException("Class not found"));

            List<Question> questions = questionRepo.findByQuizName(quizClass);
            questionRepo.deleteAll(questions);

            return new ResponseEntity<>("All questions for class " + quizsName + " deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateQuestionPartially(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        try {
            Question existingQuestion = questionRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            updates.forEach((key, value) -> {
                switch (key) {
                    case "questionTitle":
                        existingQuestion.setQuestionTitle((String) value);
                        break;
                    case "option1":
                        existingQuestion.setOption1((String) value);
                        break;
                    case "option2":
                        existingQuestion.setOption2((String) value);
                        break;
                    case "option3":
                        existingQuestion.setOption3((String) value);
                        break;
                    case "option4":
                        existingQuestion.setOption4((String) value);
                        break;
                    case "rightAnswer":
                        existingQuestion.setRightAnswer((String) value);
                        break;
                    case "difficulty":
                        existingQuestion.setDifficulty((String) value);
                        break;
                    case "category":
                        existingQuestion.setCategory((String) value);
                        break;
                }
            });

            questionRepo.save(existingQuestion);
            return new ResponseEntity<>(existingQuestion, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
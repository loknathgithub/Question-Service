package com.loknath.question_services.controllers;

import com.loknath.question_services.dao.ClassRepository;
import com.loknath.question_services.models.QuizName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/class")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class QuizClassController {
    @Autowired
    private ClassRepository classRepository;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Hash password
    public static String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    // Verify password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClass(@RequestBody QuizName quizName) {
        // Check if the class already exists in the database
        Optional<QuizName> existingClass = classRepository.findByQuizsName(quizName.getQuizsName());

        if (existingClass.isPresent()) {
            // Redirect to the internal API endpoint if the class already exists
            URI redirectUri = URI.create("http://localhost:8080/allquestions");
            return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
        }

        quizName.setPassword(hashPassword(quizName.getPassword()));
        // Return the saved class with a 200 OK status
        return ResponseEntity.ok(classRepository.save(quizName));
    }


    //verifying password
    @PostMapping("/verify")
    public ResponseEntity<?> verifyClass(
            @RequestParam String quizName,
            @RequestParam String password) {

        System.out.println("Classname: " + quizName + "\nPassword: " + password);

        // 1. Check if class exists
        Optional<QuizName> existingClass = classRepository.findByQuizsName(quizName);

        // 2. Handle new class creation
        if (existingClass.isEmpty()) {
            try {
                QuizName newQuiz = new QuizName();
                newQuiz.setQuizsName(quizName);
                newQuiz.setPassword(hashPassword(password));

                QuizName savedQuiz = classRepository.save(newQuiz);
                System.out.println("✅ New class created: " + quizName);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("New class created successfully");

            } catch (Exception e) {
                System.err.println("❌ Class creation failed: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Class creation failed");
            }
        }

        // 3. Verify existing class password
        if (checkPassword(password, existingClass.get().getPassword())) {
            System.out.println("✅ Authentication successful: " + quizName);
            return ResponseEntity.ok().body("Success: Redirecting to /allquestions");
        } else {
            System.out.println("❌ Incorrect password for: " + quizName);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect password");
        }
    }



}
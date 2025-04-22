package com.loknath.question_services.models;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_classes")  // ✅ Ensure correct table name
public class QuizName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_name", unique = true, nullable = false) // ✅ Ensure correct column name
    private String quizsName;

    @Column(nullable = false) // ✅ Ensure password is stored
    private String password;

    // Constructors
    public QuizName() {}

    public QuizName(String quizsName) {
        this.quizsName = quizsName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizsName() { // ✅ Fixed Getter Method
        return quizsName;
    }

    public void setQuizsName(String quizsName) {
        this.quizsName = quizsName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

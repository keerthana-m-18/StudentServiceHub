package com.studenthub.studentservicehub.service;

import com.studenthub.studentservicehub.model.Student;
import com.studenthub.studentservicehub.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    // --- REMOVE THE STATIC DEMO_PASSWORD FIELD ---
    // private static final String DEMO_PASSWORD = "password123";
    // This field is no longer needed as the password is now fetched from the DB.

    private final StudentRepository studentRepository;

    @Autowired
    public LoginService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Authenticates a user by checking the entered password against the unique
     * password_hash stored in the database for the given email.
     */
    public Optional<Student> authenticate(String email, String password) {
        // 1. Find user by email
        Student student = studentRepository.findByEmail(email);

        if (student != null) {
            // 2. ENFORCE UNIQUE PASSWORD MATCH (CRITICAL FIX)
            // Checks if the entered password matches the password stored in the DB's password_hash column.
            if (password.equals(student.getPasswordHash())) {
                System.out.println("LOGIN: Successfully authenticated user: " + email);
                return Optional.of(student);
            }
        }
        // If authentication fails (student is null OR password is wrong)
        System.out.println("LOGIN: Authentication failed for user: " + email + ". Invalid credentials.");
        return Optional.empty();
    }
}
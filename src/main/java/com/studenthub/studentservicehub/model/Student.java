package com.studenthub.studentservicehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student extends User { // Inheritance: Student extends User

    // Add student-specific fields/methods here later (e.g., branch, year)

    // Note: The passwordHash field is inherited from User.java (or the @MappedSuperclass)
    // To ensure the LoginService can access the stored password:
    @Override
    public String getPasswordHash() {
        return super.getPasswordHash();
    }

    // Constructors (optional for now)
}
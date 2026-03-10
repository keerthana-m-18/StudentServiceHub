package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Custom method for login/security logic [cite: 189]
    Student findByEmail(String email);
}

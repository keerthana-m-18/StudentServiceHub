package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.StudentStopAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Required for Optional return type

/**
 * Repository for managing the link between a Student and their assigned Bus/Stop.
 */
@Repository
public interface StudentStopAssignmentRepository extends JpaRepository<StudentStopAssignment, Long> {

    /**
     * Custom method to find the assignment based on the Student ID.
     * * @param studentId The ID of the student logged in (e.g., 101).
     * @return The assignment details (Bus, Target Stop), wrapped in Optional.
     */
    Optional<StudentStopAssignment> findByStudent_UserId(Long studentId);
}
package com.studenthub.studentservicehub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student_stop_assignments")
public class StudentStopAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One: Links to the Student (Recipient of the alerts)
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Many-to-One: The specific bus this student is assigned to (Bus 1 or Bus 2)
    @ManyToOne
    @JoinColumn(name = "assigned_bus_id")
    private Bus assignedBus;

    // Many-to-One: The student's specific target stop (Stop C or Stop Z)
    @ManyToOne
    @JoinColumn(name = "target_stop_id")
    private Stop targetStop;

    // --- Constructors ---

    // Mandatory no-argument constructor for Hibernate
    public StudentStopAssignment() {
    }

    // Constructor for creating a new assignment
    public StudentStopAssignment(Student student, Bus assignedBus, Stop targetStop) {
        this.student = student;
        this.assignedBus = assignedBus;
        this.targetStop = targetStop;
    }

    // --- Getters and Setters (Encapsulation) ---

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Bus getAssignedBus() {
        return assignedBus;
    }

    public void setAssignedBus(Bus assignedBus) {
        this.assignedBus = assignedBus;
    }

    public Stop getTargetStop() {
        return targetStop;
    }

    public void setTargetStop(Stop targetStop) {
        this.targetStop = targetStop;
    }
}
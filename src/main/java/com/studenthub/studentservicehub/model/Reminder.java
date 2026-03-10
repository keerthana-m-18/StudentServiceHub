package com.studenthub.studentservicehub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reminderId; // ReminderID (PK)

    @ManyToOne
    @JoinColumn(name = "student_id") // StudentID (FK)
    private Student student;

    private String title;
    private String description;
    private LocalDateTime reminderTime;
    private String type; // e.g., "Assignment", "Exam"
    private Boolean isTriggered = false;

    // Mandatory no-argument constructor
    public Reminder() {}

    // Constructor
    public Reminder(Student student, String title, String description, LocalDateTime reminderTime, String type) {
        this.student = student;
        this.title = title;
        this.description = description;
        this.reminderTime = reminderTime;
        this.type = type;
    }

    // --- Getters and Setters (Encapsulation) ---

    public Long getReminderId() { return reminderId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getReminderTime() { return reminderTime; }
    public void setReminderTime(LocalDateTime reminderTime) { this.reminderTime = reminderTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean getIsTriggered() { return isTriggered; }
    public void setIsTriggered(Boolean isTriggered) { this.isTriggered = isTriggered; }
}

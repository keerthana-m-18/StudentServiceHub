package com.studenthub.studentservicehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
// import java.time.LocalDateTime; <--- REMOVED

@Entity
@Table(name = "email_notifications")
public class EmailNotification extends Notification {

    // Specific fields for email
    private String subject;

    // Mandatory no-argument constructor for Hibernate
    public EmailNotification() {}

    // Constructor
    public EmailNotification(String recipientEmail, String messageBody, String subject) {
        this.setRecipientEmail(recipientEmail);
        this.setMessageBody(messageBody);
        this.subject = subject;
    }

    // --- Getters and Setters ---
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
package com.studenthub.studentservicehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // <-- NEW IMPORT
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // Inject the username from application.properties to use as the 'From' address
    @Value("${spring.mail.username}") // <-- NEW FIELD
    private String senderEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            // FIX: Use the configured sender email
            message.setFrom(senderEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("--- REAL EMAIL SENT SUCCESSFULLY to: " + toEmail + " FROM: " + senderEmail + " ---");
        } catch (Exception e) {
            System.err.println("--- EMAIL FAILED TO SEND: Check application.properties credentials. Error: " + e.getMessage() + " ---");
        }
    }
}
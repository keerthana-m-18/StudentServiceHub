package com.studenthub.studentservicehub.service;

import com.studenthub.studentservicehub.model.Reminder;
import com.studenthub.studentservicehub.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulerService {

    private final ReminderRepository reminderRepository;
    private final EmailService emailService; // <-- NEW FIELD
    private static final int ADVANCE_ALERT_HOURS = 24;

    @Autowired
    public SchedulerService(ReminderRepository reminderRepository, EmailService emailService) { // <-- UPDATE CONSTRUCTOR
        this.reminderRepository = reminderRepository;
        this.emailService = emailService; // <-- INITIALIZE NEW SERVICE
    }

    /**
     * Runs every 60 seconds (60000 ms) to check for pending reminders.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processUpcomingReminders() {
        processImmediateReminders();
        processAdvanceReminders();
    }

    private void processImmediateReminders() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Reminder> overdueReminders = reminderRepository.findByIsTriggeredFalseAndReminderTimeBefore(currentTime);

        for (Reminder reminder : overdueReminders) {
            System.out.println("SCHEDULER [IMMEDIATE]: Processing overdue reminder: " + reminder.getTitle());
            sendAlert(reminder, "IMMEDIATE ALERT: Your " + reminder.getType() + " is due NOW!");
            reminder.setIsTriggered(true);
            reminderRepository.save(reminder);
        }
    }

    private void processAdvanceReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusHours(ADVANCE_ALERT_HOURS);

        List<Reminder> advanceReminders = reminderRepository.findByIsTriggeredFalseAndReminderTimeBetween(now, tomorrow);

        for (Reminder reminder : advanceReminders) {
            System.out.println("SCHEDULER [ADVANCE]: Due tomorrow: " + reminder.getTitle());
            sendAlert(reminder, "ADVANCE ALERT: Your " + reminder.getType() + " is due in less than " + ADVANCE_ALERT_HOURS + " hours!");
        }
    }

    private void sendAlert(Reminder reminder, String body) {
        // TEMPORARY FIX: Send alert to your personal, verified email for demo proof
        String recipient = "studenthub.project18@gmail.com";

        emailService.sendSimpleEmail(
                recipient, // Use your verified email address here
                "Due Date Alert: " + reminder.getTitle(),
                body
        );
    }
}
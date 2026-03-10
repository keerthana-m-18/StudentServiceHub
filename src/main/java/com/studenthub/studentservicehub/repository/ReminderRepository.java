package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    /**
     * Custom method to find reminders that are overdue/due now and haven't been triggered.
     * Used by SchedulerService for immediate processing.
     * * @param time The current time (finds reminders scheduled before this time).
     * @return List of overdue/immediate reminders.
     */
    List<Reminder> findByIsTriggeredFalseAndReminderTimeBefore(LocalDateTime time);

    /**
     * Custom method to find reminders due in the next 24 hours that haven't been triggered.
     * Used by SchedulerService for advance alerts ("Tomorrow is the due date").
     * * @param startTime The current time (start of the window).
     * @param endTime The time 24 hours from now (end of the window).
     * @return List of reminders due in the next 24 hours.
     */
    List<Reminder> findByIsTriggeredFalseAndReminderTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}

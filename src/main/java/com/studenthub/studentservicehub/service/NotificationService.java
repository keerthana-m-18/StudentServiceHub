package com.studenthub.studentservicehub.service;

import com.studenthub.studentservicehub.model.Stop;
import com.studenthub.studentservicehub.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final GPSManager gpsManager;
    private final EmailService emailService; // <-- NEW FIELD

    private static final int ALERT_THRESHOLD_MINUTES = 10;

    @Autowired
    public NotificationService(GPSManager gpsManager, EmailService emailService) { // <-- UPDATE CONSTRUCTOR
        this.gpsManager = gpsManager;
        this.emailService = emailService; // <-- INITIALIZE NEW FIELD
    }

    /**
     * Checks bus ETA and sends a real email notification if the bus is near the stop.
     */
    public boolean checkAndSendBusAlert(Student student, Long busId, Stop studentStop) {

        int eta = gpsManager.calculateETA(busId, studentStop);

        if (eta > 0 && eta <= ALERT_THRESHOLD_MINUTES) {

            // --- FINAL EMAIL SEND LOGIC ---
            String subject = "BUS ALERT: Your Campus Bus is Near!";
            String body = "Your bus is arriving at " + studentStop.getStopName() + " in approximately " + eta + " minutes. Be ready!";

            emailService.sendSimpleEmail(student.getEmail(), subject, body); // Real email send

            return true;
        }

        System.out.println("BUS CHECK: ETA is " + eta + " minutes. Still too far to send alert.");
        return false;
    }
}
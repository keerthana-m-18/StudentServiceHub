package com.studenthub.studentservicehub.controller;

import com.studenthub.studentservicehub.model.Reminder;
import com.studenthub.studentservicehub.model.Student;
import com.studenthub.studentservicehub.repository.ReminderRepository;
import com.studenthub.studentservicehub.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ReminderController {

    private final ReminderRepository reminderRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ReminderController(ReminderRepository reminderRepository, StudentRepository studentRepository) {
        this.reminderRepository = reminderRepository;
        this.studentRepository = studentRepository;
    }

    // Handles the GET request to view the dedicated Reminders page
    @GetMapping("/reminders/view")
    public String viewRemindersModule(Model model) {
        // Fetch data required for the reminder-view.html template
        model.addAttribute("reminders", reminderRepository.findAll());

        // Returns the dedicated template file
        return "reminder-view";
    }

    // Handles the POST request for adding a new reminder
    @PostMapping("/reminders/add")
    public String addReminder(@RequestParam Long studentId,
                              @RequestParam String title,
                              @RequestParam String timeString,
                              RedirectAttributes redirectAttributes) { // Added RedirectAttributes

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found. Cannot save reminder."));

        LocalDateTime reminderTime = LocalDateTime.parse(timeString);

        Reminder reminder = new Reminder(student, title, "Academic Alert", reminderTime, "Assignment");
        reminderRepository.save(reminder);

        // Redirect with success message
        String successMsg = "Reminder '" + title + "' saved successfully. Scheduler will check for alerts.";
        redirectAttributes.addAttribute("statusMessage", successMsg);
        redirectAttributes.addAttribute("statusType", "success");

        // Redirect back to the dashboard, as the dedicated view has no built-in status display
        return "redirect:/dashboard";
    }
}
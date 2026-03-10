package com.studenthub.studentservicehub.controller;

import com.studenthub.studentservicehub.model.Item;
import com.studenthub.studentservicehub.model.Student;
import com.studenthub.studentservicehub.model.Stop;
import com.studenthub.studentservicehub.repository.ItemRepository;
import com.studenthub.studentservicehub.repository.ReminderRepository;
import com.studenthub.studentservicehub.repository.StopRepository;
import com.studenthub.studentservicehub.service.GPSManager;
import com.studenthub.studentservicehub.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final LoginService loginService;
    private final GPSManager gpsManager;
    private final ReminderRepository reminderRepository;
    private final ItemRepository itemRepository;
    private final StopRepository stopRepository;

    @Autowired
    public MainController(
            LoginService loginService,
            GPSManager gpsManager,
            ReminderRepository reminderRepository,
            ItemRepository itemRepository,
            StopRepository stopRepository
    ) {
        this.loginService = loginService;
        this.gpsManager = gpsManager;
        this.reminderRepository = reminderRepository;
        this.itemRepository = itemRepository;
        this.stopRepository = stopRepository;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // Displays the login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    // Processes the login form submission
    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Student> studentOpt = loginService.authenticate(email, password);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            // --- ROLE-BASED ACCESS CONTROL (RBAC) LOGIC ---
            if ("Admin_Stationery".equals(student.getRole())) {
                return "redirect:/admin/stationery/dashboard";
            }
            if ("Admin_Bus".equals(student.getRole())) {
                return "redirect:/admin/bus/dashboard";
            }
            // --- END ADMIN REDIRECT ---

            // Default success for Students (Test Student 101)
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid credentials. Please use the test account.");
            return "login";
        }
    }

    // --- CONSOLIDATED UNIFIED DASHBOARD (Student View) ---
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String statusMessage,
            @RequestParam(required = false) String statusType,
            Model model) {

        // Add received status variables for display
        if (statusMessage != null) {
            model.addAttribute("statusMessage", statusMessage);
            model.addAttribute("statusType", statusType);
        }

        // 1. Stationery Module Data
        model.addAttribute("availableItems", itemRepository.findAll());

        // 2. Reminder Module Data
        model.addAttribute("reminders", reminderRepository.findAll());

        // 3. Bus Module Data (Final Fix for Dashboard Preview)
        Long demoBusId = 101L;
        Long targetStopId = 3L; // Main Gate Stop ID (ID 3 from seeding)

        // CRITICAL FIX: Fetch the actual Stop entity from the DB to guarantee coordinates
        Optional<Stop> stopOpt = stopRepository.findById(targetStopId);

        try {
            if (stopOpt.isPresent()) {
                Stop studentStop = stopOpt.get(); // The fully loaded Stop entity

                int etaMinutes = gpsManager.calculateETA(demoBusId, studentStop);

                model.addAttribute("busId", demoBusId);
                model.addAttribute("stopName", studentStop.getStopName());
                model.addAttribute("eta", etaMinutes);
                model.addAttribute("busStatus", etaMinutes > 0 ? "LIVE" : "INACTIVE");
            } else {
                // Fallback if the required test stop (ID 3) is missing from the DB
                model.addAttribute("busId", 0L);
                model.addAttribute("busStatus", "ERROR: Stop data missing from DB.");
            }
        } catch (Exception e) {
            // Safely handle any calculation or runtime errors and prevent server crash
            System.err.println("Dashboard Bus Preview Failed: " + e.getMessage());
            model.addAttribute("busId", 0L);
            model.addAttribute("busStatus", "ERROR");
        }

        return "unified-dashboard";
    }
}
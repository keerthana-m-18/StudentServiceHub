package com.studenthub.studentservicehub.controller;

import com.studenthub.studentservicehub.model.Bus;
import com.studenthub.studentservicehub.model.Stop;
import com.studenthub.studentservicehub.model.Student;
import com.studenthub.studentservicehub.repository.BusRepository;
import com.studenthub.studentservicehub.repository.StopRepository;
import com.studenthub.studentservicehub.service.GPSManager;
import com.studenthub.studentservicehub.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class BusController {

    private final GPSManager gpsManager;
    private final NotificationService notificationService;
    private final BusRepository busRepository;
    private final StopRepository stopRepository;

    @Autowired
    public BusController(GPSManager gpsManager, NotificationService notificationService, BusRepository busRepository, StopRepository stopRepository) {
        this.gpsManager = gpsManager;
        this.notificationService = notificationService;
        this.busRepository = busRepository;
        this.stopRepository = stopRepository;
    }

    // Handles the GET request to show the list of buses (The initial selection page)
    @GetMapping("/bus/view")
    public String viewBusModule(Model model) {
        // Fetches all buses to populate the user's selection dropdown
        model.addAttribute("allBuses", busRepository.findAll());

        return "bus-selection-view";
    }

    // --- Handles the tracking request (Calculation + Output) ---
    @GetMapping("/bus/track")
    public String trackBus(
            @RequestParam Long busId, // Bus selected by user
            @RequestParam Long targetStopId, // Student's target stop ID
            Model model) {

        // 1. Fetch the actual Stop entity using the ID provided by the form
        Optional<Stop> stopOpt = stopRepository.findById(targetStopId);

        if (stopOpt.isEmpty()) {
            // Handle error case if stop data is missing
            model.addAttribute("status", "ERROR");
            model.addAttribute("stopName", "Stop ID " + targetStopId + " not found.");
            model.addAttribute("eta", -1);
            model.addAttribute("allBuses", busRepository.findAll());
            return "bus-tracking-view";
        }

        Stop studentStop = stopOpt.get();

        // 2. Setup Student data for logic (needed for the email recipient)
        Student simulatedStudent = new Student();
        simulatedStudent.setEmail("studenthub.project18@gmail.com");

        // 3. Execute ETA logic (returns 0 for Arrived, -99 for Passed, or positive minutes)
        int etaMinutes = gpsManager.calculateETA(busId, studentStop);

        // --- FINAL FIX: Translate the numerical code into the descriptive status string ---
        String statusText;
        if (etaMinutes == 0) {
            statusText = "ARRIVED"; // Bus is at the stop
        } else if (etaMinutes == -99) {
            statusText = "PASSED"; // Bus has already passed the stop
        } else if (etaMinutes > 0) {
            statusText = "LIVE"; // Bus is approaching (normal ETA)
            // 4. Check and send alert only if LIVE
            notificationService.checkAndSendBusAlert(simulatedStudent, busId, studentStop);
        } else {
            statusText = "INACTIVE"; // ETA is -1 (Bus not found)
        }
        // ----------------------------------------------------------------------------------

        // Fetch buses again so the selection dropdown remains populated on the results page
        model.addAttribute("allBuses", busRepository.findAll());

        // Prepare final status variables for the template output
        model.addAttribute("busId", busId);
        model.addAttribute("stopName", studentStop.getStopName());
        model.addAttribute("eta", etaMinutes);
        model.addAttribute("status", statusText); // <-- Send the descriptive status

        return "bus-tracking-view";
    }
}
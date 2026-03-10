package com.studenthub.studentservicehub.controller;

import com.studenthub.studentservicehub.model.Stop;
import com.studenthub.studentservicehub.model.Bus;
import com.studenthub.studentservicehub.repository.BusRepository;
import com.studenthub.studentservicehub.repository.StopRepository;
import com.studenthub.studentservicehub.service.GPSManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/admin/bus")
public class BusAdminController {

    private final GPSManager gpsManager;
    private final BusRepository busRepository;
    private final StopRepository stopRepository;

    @Autowired
    public BusAdminController(GPSManager gpsManager, BusRepository busRepository, StopRepository stopRepository) {
        this.gpsManager = gpsManager;
        this.busRepository = busRepository;
        this.stopRepository = stopRepository;
    }

    /**
     * Loads the admin dashboard, fetching the bus list and all stops for the dropdown.
     */
    @GetMapping("/dashboard")
    public String showBusAdminDashboard(Model model) {

        // Fetch all buses for the fleet list and selection dropdown
        List<Bus> allBuses = busRepository.findAll();
        model.addAttribute("allBuses", allBuses);

        // Fetch ALL stops for the selection dropdown (Stops A, B, C, D, etc.)
        model.addAttribute("allStops", stopRepository.findAll());

        // Return view and assume status messages are added to the model by DispatcherServlet
        return "admin-bus-dashboard";
    }

    /**
     * Handles the POST request to update the bus's current location (stop reached).
     * This replaces the Lat/Lon input method entirely.
     */
    @PostMapping("/update-location")
    public String updateLocation(@RequestParam Long busId,
                                 @RequestParam Long currentStopId, // <-- FIX: Receives Stop ID
                                 RedirectAttributes redirectAttributes) {
        try {
            // 1. Fetch the Bus and Stop entities
            Bus bus = busRepository.findById(busId)
                    .orElseThrow(() -> new IllegalArgumentException("Bus not found."));

            Stop stop = stopRepository.findById(currentStopId)
                    .orElseThrow(() -> new IllegalArgumentException("Stop not found."));

            // 2. Update the Bus's current location reference
            bus.setCurrentStop(stop);
            busRepository.save(bus); // Persist the change

            // 3. Optional: Update the bus's GPS coordinates field with the stop's coordinates (for visual consistency)
            gpsManager.updateBusLocation(busId, stop.getLatitude(), stop.getLongitude());

            // Log and success message
            System.out.println("ADMIN ACTION: Bus " + busId + " status updated to STOP: " + stop.getStopName());
            redirectAttributes.addAttribute("statusMessage", "Bus ID " + busId + " location updated to " + stop.getStopName() + ".");
            redirectAttributes.addAttribute("statusType", "success");

        } catch (Exception e) {
            redirectAttributes.addAttribute("statusMessage", "Location update failed: " + e.getMessage());
            redirectAttributes.addAttribute("statusType", "error");
        }
        // Redirect back to the admin dashboard
        return "redirect:/admin/bus/dashboard";
    }
}
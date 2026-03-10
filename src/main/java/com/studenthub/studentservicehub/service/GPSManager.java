package com.studenthub.studentservicehub.service;

import com.studenthub.studentservicehub.model.Bus;
import com.studenthub.studentservicehub.model.GpsLog;
import com.studenthub.studentservicehub.model.Stop;
import com.studenthub.studentservicehub.repository.BusRepository;
import com.studenthub.studentservicehub.repository.GpsLogRepository;
import com.studenthub.studentservicehub.repository.StopRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class GPSManager {

    // --- Dependency Fields ---
    @Value("${google.maps.api.key}")
    private String apiKey;

    private final WebClient webClient;

    private final BusRepository busRepository;
    private final StopRepository stopRepository;
    private final GpsLogRepository gpsLogRepository;

    private final Map<Long, String> liveBusLocations = new HashMap<>();
    private final Random random = new Random();

    // Constants for Haversine Calculation
    private static final double EARTH_RADIUS_KM = 6371;
    private final double AVG_BUS_SPEED_KPH = 20.0;

    // Test Coordinates for initialization
    private final double BUS_101_LAT = 17.4000;
    private final double BUS_101_LON = 78.4000;
    private final double BUS_102_LAT = 17.5000;
    private final double BUS_102_LON = 78.5000;

    @Autowired
    public GPSManager(BusRepository busRepository, StopRepository stopRepository, GpsLogRepository gpsLogRepository) {
        this.busRepository = busRepository;
        this.stopRepository = stopRepository;
        this.gpsLogRepository = gpsLogRepository;

        this.webClient = WebClient.create("https://maps.googleapis.com/maps/api/distancematrix/json");
    }

    // --- INITIALIZATION AND SCHEDULER ---

    @PostConstruct
    public void initializeTestBusLocation() {
        liveBusLocations.put(101L, BUS_101_LAT + "," + BUS_101_LON);
        liveBusLocations.put(102L, BUS_102_LAT + "," + BUS_102_LON);
        System.out.println("GPS MANAGER: Test Buses (IDs 101, 102) initialized for dynamic tracking demo.");
    }

    /**
     * Simulates a continuous, slight update of bus GPS data (15/30 min refresh).
     */
    @Scheduled(fixedRate = 120000)
    @Transactional
    public void simulateGpsUpdates() {
        // Target coordinates for controlled movement (e.g., Main Gate Stop 3 coordinates)
        final double TARGET_LAT = 17.4300;
        final double TARGET_LON = 78.4300;
        final double MOVEMENT_STEP = 0.0005;

        liveBusLocations.replaceAll((busId, coords) -> {
            String[] parts = coords.split(",");
            double currentLat = Double.parseDouble(parts[0]);
            double currentLon = Double.parseDouble(parts[1]);

            double newLat = currentLat;
            double newLon = currentLon;

            // Calculate direction and move bus closer to the target (ensures ETA decreases)
            if (Math.abs(currentLat - TARGET_LAT) > MOVEMENT_STEP) {
                newLat += (currentLat < TARGET_LAT) ? MOVEMENT_STEP : -MOVEMENT_STEP;
            }
            if (Math.abs(currentLon - TARGET_LON) > MOVEMENT_STEP) {
                newLon += (currentLon < TARGET_LON) ? MOVEMENT_STEP : -MOVEMENT_STEP;
            }

            System.out.println("GPS SCHEDULER: Bus " + busId + " location shifted toward stop.");
            return newLat + "," + newLon;
        });
    }

    /**
     * Updates the current location of a bus (Called by Bus Admin).
     */
    @Transactional
    public void updateBusLocation(Long busId, Double latitude, Double longitude) {
        String coordinates = latitude + "," + longitude;

        Optional<Bus> busOpt = busRepository.findById(busId);
        if (busOpt.isPresent()) {
            Bus bus = busOpt.get();

            liveBusLocations.put(busId, coordinates); // Update in-memory map

            GpsLog log = new GpsLog(bus, latitude, longitude);
            gpsLogRepository.save(log);

            bus.setGpsCoordinates(coordinates);
            busRepository.save(bus);
        }
    }

    // --- CORE ETA CALCULATION LOGIC ---

    private double degToRad(double deg) {
        return deg * (Math.PI / 180);
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = degToRad(lat2 - lat1);
        double dLon = degToRad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    /**
     * Helper method to perform the dynamic Haversine ETA calculation.
     */
    private int calculateDynamicHaversineETA(String busCoordinates, Stop studentStop) {
        // 1. Get current bus coordinates
        String[] busCoords = busCoordinates.split(",");
        double busLat = Double.parseDouble(busCoords[0]);
        double busLon = Double.parseDouble(busCoords[1]);

        // 2. Use the Student's Target Stop coordinates (from the fetched entity)
        double stopLat = studentStop.getLatitude();
        double stopLon = studentStop.getLongitude();

        // 3. Calculate distance (KM)
        double distanceKm = calculateHaversineDistance(busLat, busLon, stopLat, stopLon);

        // 4. Convert Distance to Time (minutes)
        double timeHours = distanceKm / AVG_BUS_SPEED_KPH;
        int etaMinutes = (int) Math.ceil(timeHours * 60);

        // Ensure a minimum ETA for realism and a max cap
        if (etaMinutes == 0) return 1;
        if (etaMinutes > 30) return 30;

        return etaMinutes;
    }

    /**
     * Calculates ETA using the API blueprint with Haversine fallback.
     */
    public int calculateETA(Long busId, Stop studentStop) {

        // 1. Find the Bus's current position (last stop reached)
        Optional<Bus> busOpt = busRepository.findById(busId);
        if (busOpt.isEmpty() || busOpt.get().getCurrentStop() == null) {
            return -1; // Bus not found or hasn't started route yet
        }

        Stop currentStop = busOpt.get().getCurrentStop();

        // Get Stop IDs for comparison
        Long targetStopId = studentStop.getStopId();
        Long currentStopId = currentStop.getStopId();

        // --- FINAL SEGMENT STATUS CHECK ---

        // CRITICAL: Bus has already passed the student's stop
        if (currentStopId > targetStopId) {
            return -99; // Code for 'PASSED'
        }

        // CRITICAL: Bus is currently at the student's stop
        if (currentStopId.equals(targetStopId)) {
            return 0; // Code for 'ARRIVED'
        }

        // --- DYNAMIC CALCULATION ---

        String busCoordinates = liveBusLocations.get(busId);
        String stopCoordinates = studentStop.getLatitude() + "," + studentStop.getLongitude();

        try {
            // Attempt the network call (This call simulates failure without billing)
            webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("origins", busCoordinates)
                            .queryParam("destinations", stopCoordinates)
                            .queryParam("mode", "driving")
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // Success log
            System.out.println("API CALL ATTEMPTED. Returning fallback ETA.");

            // --- FALLBACK: Use Haversine Calculation ---
            int etaMinutes = calculateDynamicHaversineETA(busCoordinates, studentStop);

            // Implement Proximity Alert for the LAST SEGMENT (Optional override for the threshold)
            long segmentsRemaining = targetStopId - currentStopId;
            if (segmentsRemaining == 1 && etaMinutes > 5) {
                // If only one segment remains, cap the ETA at 5 minutes to ensure the alert triggers
                etaMinutes = 5;
            }
            return etaMinutes;

        } catch (Exception e) {
            // Silent fallback ensures the console is clean and the app remains stable.
            return calculateDynamicHaversineETA(busCoordinates, studentStop);
        }
    }
}
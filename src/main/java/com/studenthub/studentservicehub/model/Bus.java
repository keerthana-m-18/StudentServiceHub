package com.studenthub.studentservicehub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    private String routeName;
    private String driverName;

    // NOTE: We keep gpsCoordinates (current location string) as a redundant field for manual admin override/log viewing.
    private String gpsCoordinates;

    // --- FINAL ADDITION: Tracks the last confirmed stop the bus reached ---
    // Many-to-one relationship to the Stop entity
    @ManyToOne
    @JoinColumn(name = "current_stop_id") // Foreign key linking to the stops table
    private Stop currentStop;

    // Mandatory no-argument constructor for Hibernate
    public Bus() {
    }

    // Constructor
    public Bus(String routeName, String driverName, String gpsCoordinates) {
        this.routeName = routeName;
        this.driverName = driverName;
        this.gpsCoordinates = gpsCoordinates;
    }

    // --- Getters and Setters (Encapsulation) ---

    public Long getBusId() { return busId; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public String getGpsCoordinates() { return gpsCoordinates; }
    public void setGpsCoordinates(String gpsCoordinates) { this.gpsCoordinates = gpsCoordinates; }

    // --- Getter and Setter for the new currentStop field ---
    public Stop getCurrentStop() { return currentStop; }
    public void setCurrentStop(Stop currentStop) { this.currentStop = currentStop; }
}
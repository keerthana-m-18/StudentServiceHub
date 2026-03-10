package com.studenthub.studentservicehub.model;

import jakarta.persistence.*;

@Entity
@Table(name = "stops")
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stopId;

    // Many-to-one relationship with Bus (FK: bus_id)
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    private String stopName;
    private Double latitude;
    private Double longitude;

    // --- FINAL ADDITION: For Proximity Alert Logic ---
    private Integer arrivalRadiusMeters;

    // --- Constructors ---

    // Mandatory no-argument constructor for Hibernate
    public Stop() {
    }

    // Constructor for Controller/Service simulation
    public Stop(String stopName) {
        this.stopName = stopName;
    }

    // --- Getters and Setters (Encapsulation) ---

    public Long getStopId() {
        return stopId;
    }

    public Bus getBus() {
        return bus;
    }
    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getStopName() {
        return stopName;
    }
    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // --- Getter and Setter for the new field ---
    public Integer getArrivalRadiusMeters() {
        return arrivalRadiusMeters;
    }
    public void setArrivalRadiusMeters(Integer arrivalRadiusMeters) {
        this.arrivalRadiusMeters = arrivalRadiusMeters;
    }
}
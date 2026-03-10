package com.studenthub.studentservicehub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gps_logs")
public class GpsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    // Many-to-one relationship with Bus
    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;

    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Mandatory no-argument constructor for Hibernate
    public GpsLog() {
    }

    // Constructor
    public GpsLog(Bus bus, Double latitude, Double longitude) {
        this.bus = bus;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // --- Getters and Setters (Encapsulation) ---

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }

    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}

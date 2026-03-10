package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.GpsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsLogRepository extends JpaRepository<GpsLog, Long> {
    // Basic CRUD operations for GpsLog entities.
}

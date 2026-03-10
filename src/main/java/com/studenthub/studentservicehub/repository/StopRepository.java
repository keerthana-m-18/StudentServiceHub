package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {

    // NEW METHOD: Find all stops associated with a specific bus ID
    List<Stop> findByBus_BusId(Long busId);
}
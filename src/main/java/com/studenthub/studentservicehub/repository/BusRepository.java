package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    // Basic CRUD operations for Bus entities.
}

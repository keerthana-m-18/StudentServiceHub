package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Manages CRUD operations for the Admin entity.
}

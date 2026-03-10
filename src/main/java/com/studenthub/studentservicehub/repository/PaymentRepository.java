package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // <-- CRUCIAL IMPORT

@Repository // <--- THIS MUST BE PRESENT
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Inherits CRUD methods for Payment
}
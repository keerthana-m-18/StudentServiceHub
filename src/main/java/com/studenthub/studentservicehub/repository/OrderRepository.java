package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // <-- ADD THIS IMPORT

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders matching the given payment status (e.g., "PAID").
     */
    List<Order> findByPaymentStatus(String paymentStatus);
}
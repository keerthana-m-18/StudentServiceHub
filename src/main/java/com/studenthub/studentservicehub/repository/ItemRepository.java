package com.studenthub.studentservicehub.repository;

import com.studenthub.studentservicehub.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Manages CRUD operations for the Item entity (inventory).
}

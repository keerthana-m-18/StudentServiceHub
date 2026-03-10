package com.studenthub.studentservicehub.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin extends User {
    // Ensure this file is saved.
}

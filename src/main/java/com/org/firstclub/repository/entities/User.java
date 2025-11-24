package com.org.firstclub.repository.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "app_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    // Metrics for Tier calculation
    private Integer totalOrders = 0;
    private BigDecimal totalSpent = BigDecimal.ZERO;
}

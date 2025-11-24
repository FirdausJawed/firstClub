package com.org.firstclub.repository.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"tier_id", "plan_duration_id"})})
@Data
public class PlanPricing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tier_id")
    private Tier tier;

    @ManyToOne
    @JoinColumn(name = "plan_duration_id")
    private PlanDuration planDuration;

    private BigDecimal price;
}
package com.org.firstclub.repository.entities;

import com.org.firstclub.repository.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ManyToOne
    private Tier currentTier;

    @ManyToOne
    private PlanDuration currentPlan;

    private LocalDate startDate;
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status; // ACTIVE, EXPIRED, CANCELLED

    // CONCURRENCY HANDLING:
    // If two threads try to upgrade this sub at the same time,
    // one will fail with ObjectOptimisticLockingFailureException
    @Version
    private Long version;
}
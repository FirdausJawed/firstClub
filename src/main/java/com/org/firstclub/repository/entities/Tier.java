package com.org.firstclub.repository.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Tier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // Silver, Gold, Platinum

    // EXTENSIBILITY WIN:
    // This creates a separate table automatically to store the key-values
    @ElementCollection
    @CollectionTable(name = "tier_benefits", joinColumns = @JoinColumn(name = "tier_id"))
    @MapKeyColumn(name = "benefit_key") // The Key (e.g., "PRIORITY_SUPPORT")
    @Column(name = "benefit_value")     // The Value (e.g., "true")
    private Map<String, String> benefits = new HashMap<>();

    // Eligibility Criteria (simplified for demo)
    private Integer minOrders;
    private Double minOrderValue;
}

/*
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank(message = "Name is required") // Rejects null and "" and " "
    private String name;

    @Email(message = "Invalid email format") // Checks for @ and . structure
    @NotBlank(message = "Email is required")
    private String email;
}

@Data
public class SubscriptionRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Plan Pricing ID cannot be null")
    private Long planPricingId;

    // Example: If you allowed custom start dates
    // @FutureOrPresent(message = "Start date cannot be in the past")
    // private LocalDate startDate;
}

@Data
public class CreateTierRequest {

    @NotBlank
    private String tierName;

    @Min(value = 0, message = "Orders count cannot be negative")
    private Integer minOrders; // Prevents logical error of -5 orders

    @DecimalMin(value = "0.0", inclusive = true, message = "Order value must be positive")
    private Double minOrderValue;
}
 */
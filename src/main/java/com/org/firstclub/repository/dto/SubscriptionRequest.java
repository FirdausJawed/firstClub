package com.org.firstclub.repository.dto;

import com.org.firstclub.repository.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Plan Pricing ID cannot be null")
    private Long planPricingId;
}
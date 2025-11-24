package com.org.firstclub.controller;

import com.org.firstclub.repository.dto.SubscriptionRequest;
import com.org.firstclub.repository.dto.UserRegistrationRequest;
import com.org.firstclub.repository.entities.PlanPricing;
import com.org.firstclub.repository.entities.Subscription;
import com.org.firstclub.repository.entities.Tier;
import com.org.firstclub.repository.entities.User;
import com.org.firstclub.service.PlanService;
import com.org.firstclub.service.SubscriptionService;
import com.org.firstclub.service.TierService;
import com.org.firstclub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for membership-related operations.
 * Handles HTTP requests and delegates business logic to service layer.
 * Follows Single Responsibility Principle - only handles HTTP concerns.
 */
@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final TierService tierService;
    private final PlanService planService;

    @Autowired
    public MembershipController(SubscriptionService subscriptionService,
                                UserService userService,
                                TierService tierService,
                                PlanService planService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.tierService = tierService;
        this.planService = planService;
    }

    /**
     * Create a new user.
     *
     * @param request User registration request with name and email
     * @return Created user with HTTP 201 status
     */
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Get all users (for testing/admin purposes).
     *
     * @return List of all users
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get a specific user by ID.
     *
     * @param userId The user ID
     * @return The user
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    /**
     * Get all available membership plans (tier + duration + price combinations).
     *
     * @return List of all plan pricing options
     */
    @GetMapping("/plans")
    public ResponseEntity<List<PlanPricing>> getAvailablePlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    /**
     * Get all available tiers with their benefits.
     *
     * @return List of all tiers
     */
    @GetMapping("/tiers")
    public ResponseEntity<List<Tier>> getAllTiers() {
        return ResponseEntity.ok(tierService.getAllTiers());
    }

    /**
     * Check if a user is eligible for a specific tier.
     *
     * @param userId The user ID
     * @param tierId The tier ID
     * @return true if eligible, false otherwise
     */
    @GetMapping("/eligibility/{userId}/{tierId}")
    public ResponseEntity<Boolean> checkEligibility(@PathVariable Long userId, @PathVariable Long tierId) {
        boolean eligible = tierService.checkUserEligibility(userId, tierId);
        return ResponseEntity.ok(eligible);
    }

    /**
     * Subscribe a user to a plan.
     * Handles new subscriptions, upgrades, and downgrades automatically.
     *
     * @param request Subscription request with userId and planPricingId
     * @return The created or updated subscription
     */
    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribe(@Valid @RequestBody SubscriptionRequest request) {
        Subscription subscription = subscriptionService.subscribe(
                request.getUserId(),
                request.getPlanPricingId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(subscription);
    }

    /**
     * Get the active subscription for a user.
     *
     * @param userId The user ID
     * @return The active subscription with tier and expiry information
     */
    @GetMapping("/status/{userId}")
    public ResponseEntity<Subscription> getSubscriptionStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(userId));
    }

    /**
     * Cancel an active subscription for a user.
     *
     * @param userId The user ID
     * @return HTTP 204 No Content on success
     */
    @PostMapping("/cancel/{userId}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long userId) {
        subscriptionService.cancelSubscription(userId);
        return ResponseEntity.noContent().build();
    }
}

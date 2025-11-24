package com.org.firstclub.service;

import com.org.firstclub.exception.ResourceNotFoundException;
import com.org.firstclub.exception.SubscriptionException;
import com.org.firstclub.exception.UserNotEligibleException;
import com.org.firstclub.repository.PlanPricingRepository;
import com.org.firstclub.repository.SubscriptionRepository;
import com.org.firstclub.repository.UserRepository;
import com.org.firstclub.repository.entities.PlanPricing;
import com.org.firstclub.repository.entities.Subscription;
import com.org.firstclub.repository.entities.User;
import com.org.firstclub.repository.enums.SubscriptionStatus;
import com.org.firstclub.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service class for managing subscriptions.
 * Handles subscription-related business logic including subscribe, upgrade, downgrade, and cancel operations.
 */
@Service
@Transactional
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final PlanPricingRepository planPricingRepository;
    private final UserRepository userRepository;
    private final CommonUtils commonUtils;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               PlanPricingRepository planPricingRepository,
                               UserRepository userRepository,
                               CommonUtils commonUtils) {
        this.subscriptionRepository = subscriptionRepository;
        this.planPricingRepository = planPricingRepository;
        this.userRepository = userRepository;
        this.commonUtils = commonUtils;
    }

    /**
     * Subscribe a user to a plan by user ID and plan pricing ID.
     * Handles new subscriptions, upgrades, and downgrades.
     *
     * @param userId The user ID
     * @param planPricingId The plan pricing ID
     * @return The created or updated subscription
     * @throws ResourceNotFoundException if user or plan not found
     * @throws UserNotEligibleException if user doesn't meet tier requirements
     */
    public Subscription subscribe(Long userId, Long planPricingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return subscribe(user, planPricingId);
    }

    /**
     * Subscribe a user to a plan.
     * Handles new subscriptions, upgrades, and downgrades.
     *
     * @param user The user
     * @param planPricingId The plan pricing ID
     * @return The created or updated subscription
     * @throws ResourceNotFoundException if plan not found
     * @throws UserNotEligibleException if user doesn't meet tier requirements
     */
    public Subscription subscribe(User user, Long planPricingId) {
        PlanPricing pricing = planPricingRepository.findById(planPricingId)
                .orElseThrow(() -> new ResourceNotFoundException("Plan Pricing", planPricingId));

        // 1. Validate Eligibility
        if (!commonUtils.isEligible(user, pricing.getTier())) {
            String tierName = pricing.getTier().getName();
            Integer minOrders = pricing.getTier().getMinOrders();
            String reason = String.format("Requires minimum %d orders or minimum order value", minOrders);
            throw new UserNotEligibleException(tierName, reason);
        }

        // 2. Check existing subscription
        Optional<Subscription> existing = subscriptionRepository.findByUserIdAndStatus(
                user.getId(), SubscriptionStatus.ACTIVE);

        if (existing.isPresent()) {
            logger.info("User {} has active subscription. Handling upgrade/downgrade to plan {}",
                    user.getId(), planPricingId);
            return handleUpgrade(existing.get(), pricing);
        }

        // 3. Create New Subscription
        logger.info("Creating new subscription for user {} with plan {}", user.getId(), planPricingId);
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCurrentTier(pricing.getTier());
        subscription.setCurrentPlan(pricing.getPlanDuration());
        subscription.setStartDate(LocalDate.now());
        subscription.setExpiryDate(LocalDate.now().plusDays(pricing.getPlanDuration().getDurationInDays()));
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        return subscriptionRepository.save(subscription);
    }

    /**
     * Handle upgrade or downgrade of an existing subscription.
     * Cancels the current subscription and creates a new one.
     * Uses optimistic locking (@Version) to prevent race conditions.
     *
     * @param current The current active subscription
     * @param newPlan The new plan pricing
     * @return The new subscription
     */
    private Subscription handleUpgrade(Subscription current, PlanPricing newPlan) {
        logger.info("Upgrading/Downgrading subscription {} from tier {} to tier {}",
                current.getId(),
                current.getCurrentTier().getName(),
                newPlan.getTier().getName());

        // Update the existing subscription instead of creating a new one
        // This avoids unique constraint violation on user_id
        current.setCurrentTier(newPlan.getTier());
        current.setCurrentPlan(newPlan.getPlanDuration());
        current.setStartDate(LocalDate.now());
        current.setExpiryDate(LocalDate.now().plusDays(newPlan.getPlanDuration().getDurationInDays()));
        current.setStatus(SubscriptionStatus.ACTIVE);

        return subscriptionRepository.save(current);
    }

    /**
     * Cancel an active subscription for a user.
     *
     * @param userId The user ID
     * @throws SubscriptionException if no active subscription found
     */
    public void cancelSubscription(Long userId) {
        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new SubscriptionException("No active subscription found for user ID: " + userId));

        logger.info("Cancelling subscription {} for user {}", subscription.getId(), userId);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);
    }

    /**
     * Get the active subscription for a user.
     * Includes lazy expiration check - if subscription has expired but status is still ACTIVE,
     * it will be updated to EXPIRED.
     *
     * @param userId The user ID
     * @return The active subscription
     * @throws SubscriptionException if no active subscription or subscription has expired
     */
    public Subscription getActiveSubscription(Long userId) {
        // Fetch the subscription marked as active in DB
        Subscription subscription = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new SubscriptionException("No active subscription found for user ID: " + userId));

        // Lazy expiration check
        // Handles edge case where subscription expired but hasn't been updated yet
        if (subscription.getExpiryDate().isBefore(LocalDate.now())) {
            logger.warn("Found expired subscription {} marked as ACTIVE. Updating status to EXPIRED.",
                    subscription.getId());

            subscription.setStatus(SubscriptionStatus.EXPIRED);
            subscriptionRepository.save(subscription);

            throw new SubscriptionException("Subscription has expired on " + subscription.getExpiryDate());
        }

        return subscription;
    }
}

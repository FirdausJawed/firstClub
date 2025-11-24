package com.org.firstclub.service;

import com.org.firstclub.exception.ResourceNotFoundException;
import com.org.firstclub.exception.SubscriptionException;
import com.org.firstclub.exception.UserNotEligibleException;
import com.org.firstclub.repository.PlanPricingRepository;
import com.org.firstclub.repository.SubscriptionRepository;
import com.org.firstclub.repository.UserRepository;
import com.org.firstclub.repository.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private PlanPricingRepository planPricingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User testUser;
    private Tier silverTier;
    private Tier goldTier;
    private PlanDuration monthlyDuration;
    private PlanDuration yearlyDuration;
    private PlanPricing silverMonthly;
    private PlanPricing goldYearly;
    private Subscription activeSubscription;

    @BeforeEach
    void setUp() {
        // Setup user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setTotalOrders(10);
        testUser.setTotalSpent(BigDecimal.valueOf(1000));

        // Setup tiers
        silverTier = new Tier();
        silverTier.setId(1L);
        silverTier.setName("Silver");
        silverTier.setMinOrders(0);
        silverTier.setMinOrderValue(0.0);
        silverTier.setBenefits(new HashMap<>());

        goldTier = new Tier();
        goldTier.setId(2L);
        goldTier.setName("Gold");
        goldTier.setMinOrders(5);
        goldTier.setMinOrderValue(500.0);
        goldTier.setBenefits(new HashMap<>());

        // Setup durations
        monthlyDuration = new PlanDuration();
        monthlyDuration.setId(1L);
        monthlyDuration.setName("Monthly");
        monthlyDuration.setDurationInDays(30);

        yearlyDuration = new PlanDuration();
        yearlyDuration.setId(2L);
        yearlyDuration.setName("Yearly");
        yearlyDuration.setDurationInDays(365);

        // Setup plan pricings
        silverMonthly = new PlanPricing();
        silverMonthly.setId(1L);
        silverMonthly.setTier(silverTier);
        silverMonthly.setPlanDuration(monthlyDuration);
        silverMonthly.setPrice(BigDecimal.valueOf(99));

        goldYearly = new PlanPricing();
        goldYearly.setId(2L);
        goldYearly.setTier(goldTier);
        goldYearly.setPlanDuration(yearlyDuration);
        goldYearly.setPrice(BigDecimal.valueOf(999));

        // Setup active subscription
        activeSubscription = new Subscription();
        activeSubscription.setId(1L);
        activeSubscription.setUser(testUser);
        activeSubscription.setCurrentTier(silverTier);
        activeSubscription.setCurrentPlan(monthlyDuration);
        activeSubscription.setStartDate(LocalDate.now());
        activeSubscription.setExpiryDate(LocalDate.now().plusDays(30));
        activeSubscription.setStatus(SubscriptionStatus.ACTIVE);
        activeSubscription.setVersion(0L);
    }

    @Test
    void subscribe_NewSubscription_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(planPricingRepository.findById(1L)).thenReturn(Optional.of(silverMonthly));
        when(subscriptionRepository.findByUserAndStatus(testUser, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(activeSubscription);

        Subscription result = subscriptionService.subscribe(1L, 1L);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void subscribe_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.subscribe(999L, 1L);
        });

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void subscribe_PlanNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(planPricingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.subscribe(1L, 999L);
        });

        verify(planPricingRepository, times(1)).findById(999L);
    }

    @Test
    void subscribe_UserNotEligible() {
        User ineligibleUser = new User();
        ineligibleUser.setId(2L);
        ineligibleUser.setTotalOrders(0);
        ineligibleUser.setTotalSpent(BigDecimal.ZERO);

        when(userRepository.findById(2L)).thenReturn(Optional.of(ineligibleUser));
        when(planPricingRepository.findById(2L)).thenReturn(Optional.of(goldYearly));

        assertThrows(UserNotEligibleException.class, () -> {
            subscriptionService.subscribe(2L, 2L);
        });
    }

    @Test
    void subscribe_WithActiveSubscription_Upgrade() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(planPricingRepository.findById(2L)).thenReturn(Optional.of(goldYearly));
        when(subscriptionRepository.findByUserAndStatus(testUser, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.of(activeSubscription));
        
        Subscription upgradedSubscription = new Subscription();
        upgradedSubscription.setId(1L);
        upgradedSubscription.setUser(testUser);
        upgradedSubscription.setCurrentTier(goldTier);
        upgradedSubscription.setCurrentPlan(yearlyDuration);
        upgradedSubscription.setStatus(SubscriptionStatus.ACTIVE);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(upgradedSubscription);

        Subscription result = subscriptionService.subscribe(1L, 2L);

        assertNotNull(result);
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void cancel_Success() {
        when(subscriptionRepository.findByUserIdAndStatus(1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.of(activeSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(activeSubscription);

        subscriptionService.cancel(1L);

        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void cancel_NoActiveSubscription() {
        when(subscriptionRepository.findByUserIdAndStatus(1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(SubscriptionException.class, () -> {
            subscriptionService.cancel(1L);
        });
    }

    @Test
    void getActiveSubscription_Success() {
        when(subscriptionRepository.findByUserIdAndStatus(1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.of(activeSubscription));

        Subscription result = subscriptionService.getActiveSubscription(1L);

        assertNotNull(result);
        assertEquals(SubscriptionStatus.ACTIVE, result.getStatus());
        verify(subscriptionRepository, times(1)).findByUserIdAndStatus(1L, SubscriptionStatus.ACTIVE);
    }

    @Test
    void getActiveSubscription_Expired() {
        activeSubscription.setExpiryDate(LocalDate.now().minusDays(1));
        when(subscriptionRepository.findByUserIdAndStatus(1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.of(activeSubscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(activeSubscription);

        Subscription result = subscriptionService.getActiveSubscription(1L);

        assertNotNull(result);
        assertEquals(SubscriptionStatus.EXPIRED, result.getStatus());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void getActiveSubscription_NotFound() {
        when(subscriptionRepository.findByUserIdAndStatus(1L, SubscriptionStatus.ACTIVE))
                .thenReturn(Optional.empty());

        assertThrows(SubscriptionException.class, () -> {
            subscriptionService.getActiveSubscription(1L);
        });
    }
}


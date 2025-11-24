package com.org.firstclub.utils;

import com.org.firstclub.repository.entities.Tier;
import com.org.firstclub.repository.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CommonUtilsTest {

    private Tier silverTier;
    private Tier goldTier;
    private Tier platinumTier;

    @BeforeEach
    void setUp() {
        silverTier = new Tier();
        silverTier.setName("Silver");
        silverTier.setMinOrders(0);
        silverTier.setMinOrderValue(0.0);

        goldTier = new Tier();
        goldTier.setName("Gold");
        goldTier.setMinOrders(5);
        goldTier.setMinOrderValue(500.0);

        platinumTier = new Tier();
        platinumTier.setName("Platinum");
        platinumTier.setMinOrders(20);
        platinumTier.setMinOrderValue(2000.0);
    }

    @Test
    void isEligible_SilverTier_AlwaysEligible() {
        User user = new User();
        user.setTotalOrders(0);
        user.setTotalSpent(BigDecimal.ZERO);

        boolean result = CommonUtils.isEligible(user, silverTier);

        assertTrue(result);
    }

    @Test
    void isEligible_GoldTier_EligibleByOrders() {
        User user = new User();
        user.setTotalOrders(10);
        user.setTotalSpent(BigDecimal.valueOf(100));

        boolean result = CommonUtils.isEligible(user, goldTier);

        assertTrue(result);
    }

    @Test
    void isEligible_GoldTier_EligibleBySpent() {
        User user = new User();
        user.setTotalOrders(2);
        user.setTotalSpent(BigDecimal.valueOf(600));

        boolean result = CommonUtils.isEligible(user, goldTier);

        assertTrue(result);
    }

    @Test
    void isEligible_GoldTier_NotEligible() {
        User user = new User();
        user.setTotalOrders(3);
        user.setTotalSpent(BigDecimal.valueOf(300));

        boolean result = CommonUtils.isEligible(user, goldTier);

        assertFalse(result);
    }

    @Test
    void isEligible_PlatinumTier_EligibleByOrders() {
        User user = new User();
        user.setTotalOrders(25);
        user.setTotalSpent(BigDecimal.valueOf(1000));

        boolean result = CommonUtils.isEligible(user, platinumTier);

        assertTrue(result);
    }

    @Test
    void isEligible_PlatinumTier_EligibleBySpent() {
        User user = new User();
        user.setTotalOrders(10);
        user.setTotalSpent(BigDecimal.valueOf(2500));

        boolean result = CommonUtils.isEligible(user, platinumTier);

        assertTrue(result);
    }

    @Test
    void isEligible_PlatinumTier_NotEligible() {
        User user = new User();
        user.setTotalOrders(15);
        user.setTotalSpent(BigDecimal.valueOf(1500));

        boolean result = CommonUtils.isEligible(user, platinumTier);

        assertFalse(result);
    }

    @Test
    void isEligible_ExactlyAtThreshold_Orders() {
        User user = new User();
        user.setTotalOrders(5);
        user.setTotalSpent(BigDecimal.ZERO);

        boolean result = CommonUtils.isEligible(user, goldTier);

        assertTrue(result);
    }

    @Test
    void isEligible_ExactlyAtThreshold_Spent() {
        User user = new User();
        user.setTotalOrders(0);
        user.setTotalSpent(BigDecimal.valueOf(500));

        boolean result = CommonUtils.isEligible(user, goldTier);

        assertTrue(result);
    }
}


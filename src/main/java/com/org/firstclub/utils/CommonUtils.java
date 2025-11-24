package com.org.firstclub.utils;

import com.org.firstclub.repository.entities.Tier;
import com.org.firstclub.repository.entities.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Utility class for common business logic operations.
 * Contains reusable methods that don't fit into specific service classes.
 */
@Component
public class CommonUtils {

    /**
     * Checks if a user qualifies for a specific target tier based on defined criteria.
     * User is eligible if they meet EITHER the minimum orders OR minimum order value requirement.
     *
     * @param user The user to check
     * @param targetTier The tier to check eligibility for
     * @return true if user meets eligibility criteria, false otherwise
     */
    public boolean isEligible(User user, Tier targetTier) {
        // Extensible design: This could be refactored to use Strategy pattern
        // with a list of 'EligibilityRule' interfaces for more complex criteria
        boolean meetsOrderRequirement = user.getTotalOrders() >= targetTier.getMinOrders();
        boolean meetsValueRequirement = user.getTotalSpent()
                .compareTo(BigDecimal.valueOf(targetTier.getMinOrderValue())) >= 0;

        // User is eligible if they meet EITHER requirement (OR logic)
        return meetsOrderRequirement || meetsValueRequirement;
    }
}


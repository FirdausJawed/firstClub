package com.org.firstclub.utils;

import com.org.firstclub.repository.PlanDurationRepository;
import com.org.firstclub.repository.PlanPricingRepository;
import com.org.firstclub.repository.TierRepository;
import com.org.firstclub.repository.entities.PlanDuration;
import com.org.firstclub.repository.entities.PlanPricing;
import com.org.firstclub.repository.entities.Tier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private TierRepository tierRepo;
    @Autowired private PlanDurationRepository durationRepo;
    @Autowired private PlanPricingRepository pricingRepo;

    // Constants for Benefit Keys (To avoid typos)
    private static final String BENEFIT_FREE_DELIVERY = "FREE_DELIVERY";
    private static final String BENEFIT_DISCOUNT_PCT = "DISCOUNT_PERCENT";
    private static final String BENEFIT_PRIORITY_SUPPORT = "PRIORITY_SUPPORT";
    private static final String BENEFIT_EARLY_ACCESS = "EARLY_ACCESS_HOURS";

    @Override
    public void run(String... args) throws Exception {
// 1. Guard Clause: Don't run if DB is already populated
        if (tierRepo.count() > 0) {
            System.out.println("⚡ Database already seeded. Skipping initialization.");
            return;
        }

        System.out.println("🌱 Starting Database Seeding...");

        // ==========================================
        // STEP 1: Create Plan Durations (Time)
        // ==========================================
        PlanDuration monthly = createDuration("Monthly", 30);
        PlanDuration quarterly = createDuration("Quarterly", 90);
        PlanDuration yearly = createDuration("Yearly", 365);

        // ==========================================
        // STEP 2: Create Tiers with Logic & Benefits
        // ==========================================

        // --- SILVER TIER (Entry Level) ---
        Map<String, String> silverBenefits = new HashMap<>();
        silverBenefits.put(BENEFIT_FREE_DELIVERY, "true");
        silverBenefits.put(BENEFIT_DISCOUNT_PCT, "0"); // No extra discount

        // Silver Logic: Open to everyone (0 orders)
        Tier silver = createTier("Silver", 0, 0.0, silverBenefits);


        // --- GOLD TIER (Mid Level) ---
        Map<String, String> goldBenefits = new HashMap<>();
        goldBenefits.put(BENEFIT_FREE_DELIVERY, "true");
        goldBenefits.put(BENEFIT_DISCOUNT_PCT, "5"); // 5% off
        goldBenefits.put(BENEFIT_EARLY_ACCESS, "24"); // 24 hours early access

        // Gold Logic: Need 5 orders OR spent $500
        Tier gold = createTier("Gold", 5, 500.00, goldBenefits);


        // --- PLATINUM TIER (VIP Level) ---
        Map<String, String> platinumBenefits = new HashMap<>();
        platinumBenefits.put(BENEFIT_FREE_DELIVERY, "true");
        platinumBenefits.put(BENEFIT_DISCOUNT_PCT, "10"); // 10% off
        platinumBenefits.put(BENEFIT_EARLY_ACCESS, "48"); // 48 hours early access
        platinumBenefits.put(BENEFIT_PRIORITY_SUPPORT, "true"); // Dedicated agent

        // Platinum Logic: Need 20 orders OR spent $2000
        Tier platinum = createTier("Platinum", 20, 2000.00, platinumBenefits);

        // ==========================================
        // STEP 3: Create Pricing (The 3x3 Matrix)
        // ==========================================

        // Silver Prices
        createPricing(silver, monthly, "9.99");
        createPricing(silver, quarterly, "24.99");  // Slight savings
        createPricing(silver, yearly, "99.99");     // ~2 months free

        // Gold Prices
        createPricing(gold, monthly, "19.99");
        createPricing(gold, quarterly, "49.99");
        createPricing(gold, yearly, "179.99");

        // Platinum Prices
        createPricing(platinum, monthly, "49.99");
        createPricing(platinum, quarterly, "129.99");
        createPricing(platinum, yearly, "499.99");

        System.out.println("✅ Database Seeding Completed Successfully!");
    }

    // ==========================================
    // HELPER METHODS (Keeps the main logic clean)
    // ==========================================

    private PlanDuration createDuration(String name, int days) {
        PlanDuration duration = new PlanDuration();
        duration.setName(name);
        duration.setDurationInDays(days);
        return durationRepo.save(duration);
    }

    private Tier createTier(String name, Integer minOrders, Double minSpend, Map<String, String> benefits) {
        Tier tier = new Tier();
        tier.setName(name);
        tier.setMinOrders(minOrders);
        tier.setMinOrderValue(minSpend);
        tier.setBenefits(benefits); // Assumes you used @ElementCollection in Tier entity
        return tierRepo.save(tier);
    }

    private void createPricing(Tier tier, PlanDuration duration, String priceStr) {
        PlanPricing pricing = new PlanPricing();
        pricing.setTier(tier);
        pricing.setPlanDuration(duration);
        pricing.setPrice(new BigDecimal(priceStr));
        pricingRepo.save(pricing);
    }
}

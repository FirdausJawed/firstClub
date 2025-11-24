package com.org.firstclub.service;

import com.org.firstclub.repository.PlanPricingRepository;
import com.org.firstclub.repository.entities.PlanDuration;
import com.org.firstclub.repository.entities.PlanPricing;
import com.org.firstclub.repository.entities.Tier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanPricingRepository planPricingRepository;

    @InjectMocks
    private PlanService planService;

    private PlanPricing planPricing1;
    private PlanPricing planPricing2;

    @BeforeEach
    void setUp() {
        Tier silverTier = new Tier();
        silverTier.setId(1L);
        silverTier.setName("Silver");

        PlanDuration monthly = new PlanDuration();
        monthly.setId(1L);
        monthly.setName("Monthly");
        monthly.setDurationInDays(30);

        PlanDuration yearly = new PlanDuration();
        yearly.setId(2L);
        yearly.setName("Yearly");
        yearly.setDurationInDays(365);

        planPricing1 = new PlanPricing();
        planPricing1.setId(1L);
        planPricing1.setTier(silverTier);
        planPricing1.setPlanDuration(monthly);
        planPricing1.setPrice(BigDecimal.valueOf(99));

        planPricing2 = new PlanPricing();
        planPricing2.setId(2L);
        planPricing2.setTier(silverTier);
        planPricing2.setPlanDuration(yearly);
        planPricing2.setPrice(BigDecimal.valueOf(999));
    }

    @Test
    void getAllPlans_Success() {
        List<PlanPricing> plans = Arrays.asList(planPricing1, planPricing2);
        when(planPricingRepository.findAll()).thenReturn(plans);

        List<PlanPricing> result = planService.getAllPlans();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(planPricing1.getId(), result.get(0).getId());
        assertEquals(planPricing2.getId(), result.get(1).getId());
        verify(planPricingRepository, times(1)).findAll();
    }

    @Test
    void getAllPlans_EmptyList() {
        when(planPricingRepository.findAll()).thenReturn(Arrays.asList());

        List<PlanPricing> result = planService.getAllPlans();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(planPricingRepository, times(1)).findAll();
    }
}


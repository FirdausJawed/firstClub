package com.org.firstclub.service;

import com.org.firstclub.repository.PlanPricingRepository;
import com.org.firstclub.repository.entities.PlanPricing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing plan pricing.
 * Handles plan-related business logic following Single Responsibility Principle.
 */
@Service
@Transactional(readOnly = true)
public class PlanService {
    
    private final PlanPricingRepository planPricingRepository;
    
    @Autowired
    public PlanService(PlanPricingRepository planPricingRepository) {
        this.planPricingRepository = planPricingRepository;
    }
    
    /**
     * Get all available plan pricing options (tier + duration + price combinations).
     * 
     * @return List of all plan pricing options
     */
    public List<PlanPricing> getAllPlans() {
        return planPricingRepository.findAll();
    }
}


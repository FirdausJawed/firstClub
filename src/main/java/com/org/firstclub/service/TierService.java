package com.org.firstclub.service;

import com.org.firstclub.exception.ResourceNotFoundException;
import com.org.firstclub.repository.TierRepository;
import com.org.firstclub.repository.UserRepository;
import com.org.firstclub.repository.entities.Tier;
import com.org.firstclub.repository.entities.User;
import com.org.firstclub.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing membership tiers.
 * Handles tier-related business logic following Single Responsibility Principle.
 */
@Service
@Transactional
public class TierService {
    
    private final TierRepository tierRepository;
    private final UserRepository userRepository;
    private final CommonUtils commonUtils;
    
    @Autowired
    public TierService(TierRepository tierRepository, 
                       UserRepository userRepository, 
                       CommonUtils commonUtils) {
        this.tierRepository = tierRepository;
        this.userRepository = userRepository;
        this.commonUtils = commonUtils;
    }
    
    /**
     * Get all available tiers with their benefits.
     * 
     * @return List of all tiers
     */
    public List<Tier> getAllTiers() {
        return tierRepository.findAll();
    }
    
    /**
     * Get a specific tier by ID.
     * 
     * @param tierId The tier ID
     * @return The tier
     * @throws ResourceNotFoundException if tier not found
     */
    public Tier getTierById(Long tierId) {
        return tierRepository.findById(tierId)
                .orElseThrow(() -> new ResourceNotFoundException("Tier", tierId));
    }
    
    /**
     * Check if a user is eligible for a specific tier.
     * 
     * @param userId The user ID
     * @param tierId The tier ID
     * @return true if user is eligible, false otherwise
     * @throws ResourceNotFoundException if user or tier not found
     */
    public boolean checkUserEligibility(Long userId, Long tierId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        
        Tier tier = tierRepository.findById(tierId)
                .orElseThrow(() -> new ResourceNotFoundException("Tier", tierId));
        
        return commonUtils.isEligible(user, tier);
    }
}


package com.org.firstclub.service;

import com.org.firstclub.exception.ResourceNotFoundException;
import com.org.firstclub.repository.TierRepository;
import com.org.firstclub.repository.UserRepository;
import com.org.firstclub.repository.entities.Tier;
import com.org.firstclub.repository.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TierServiceTest {

    @Mock
    private TierRepository tierRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TierService tierService;

    private Tier silverTier;
    private Tier goldTier;
    private User eligibleUser;
    private User ineligibleUser;

    @BeforeEach
    void setUp() {
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

        eligibleUser = new User();
        eligibleUser.setId(1L);
        eligibleUser.setName("Eligible User");
        eligibleUser.setEmail("eligible@example.com");
        eligibleUser.setTotalOrders(10);
        eligibleUser.setTotalSpent(BigDecimal.valueOf(1000));

        ineligibleUser = new User();
        ineligibleUser.setId(2L);
        ineligibleUser.setName("Ineligible User");
        ineligibleUser.setEmail("ineligible@example.com");
        ineligibleUser.setTotalOrders(2);
        ineligibleUser.setTotalSpent(BigDecimal.valueOf(100));
    }

    @Test
    void getAllTiers_Success() {
        List<Tier> tiers = Arrays.asList(silverTier, goldTier);
        when(tierRepository.findAll()).thenReturn(tiers);

        List<Tier> result = tierService.getAllTiers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tierRepository, times(1)).findAll();
    }

    @Test
    void getTierById_Success() {
        when(tierRepository.findById(1L)).thenReturn(Optional.of(silverTier));

        Tier result = tierService.getTierById(1L);

        assertNotNull(result);
        assertEquals(silverTier.getId(), result.getId());
        assertEquals(silverTier.getName(), result.getName());
        verify(tierRepository, times(1)).findById(1L);
    }

    @Test
    void getTierById_NotFound() {
        when(tierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tierService.getTierById(999L);
        });

        verify(tierRepository, times(1)).findById(999L);
    }

    @Test
    void checkUserEligibility_EligibleUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(eligibleUser));
        when(tierRepository.findById(2L)).thenReturn(Optional.of(goldTier));

        boolean result = tierService.checkUserEligibility(1L, 2L);

        assertTrue(result);
        verify(userRepository, times(1)).findById(1L);
        verify(tierRepository, times(1)).findById(2L);
    }

    @Test
    void checkUserEligibility_IneligibleUser() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(ineligibleUser));
        when(tierRepository.findById(2L)).thenReturn(Optional.of(goldTier));

        boolean result = tierService.checkUserEligibility(2L, 2L);

        assertFalse(result);
        verify(userRepository, times(1)).findById(2L);
        verify(tierRepository, times(1)).findById(2L);
    }

    @Test
    void checkUserEligibility_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tierService.checkUserEligibility(999L, 2L);
        });

        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void checkUserEligibility_TierNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(eligibleUser));
        when(tierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            tierService.checkUserEligibility(1L, 999L);
        });

        verify(userRepository, times(1)).findById(1L);
        verify(tierRepository, times(1)).findById(999L);
    }
}


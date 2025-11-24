package com.org.firstclub.service;

import com.org.firstclub.exception.ResourceNotFoundException;
import com.org.firstclub.repository.UserRepository;
import com.org.firstclub.repository.dto.UserRegistrationRequest;
import com.org.firstclub.repository.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class for managing users.
 * Handles user-related business logic following Single Responsibility Principle.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a new user.
     *
     * @param request User registration request containing name and email
     * @return The created user
     */
    public User createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setTotalOrders(0);
        user.setTotalSpent(BigDecimal.ZERO);

        return userRepository.save(user);
    }

    /**
     * Get all users.
     *
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a user by ID.
     *
     * @param userId The user ID
     * @return The user
     * @throws ResourceNotFoundException if user not found
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }
}


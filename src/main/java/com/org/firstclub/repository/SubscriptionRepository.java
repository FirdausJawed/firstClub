package com.org.firstclub.repository;

import com.org.firstclub.repository.entities.Subscription;
import com.org.firstclub.repository.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);
}

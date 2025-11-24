package com.org.firstclub.exception;

/**
 * Exception thrown when a user is not eligible for a specific tier.
 */
public class UserNotEligibleException extends RuntimeException {
    
    public UserNotEligibleException(String message) {
        super(message);
    }
    
    public UserNotEligibleException(String tierName, String reason) {
        super(String.format("User not eligible for %s tier. Reason: %s", tierName, reason));
    }
    
    public UserNotEligibleException(String message, Throwable cause) {
        super(message, cause);
    }
}


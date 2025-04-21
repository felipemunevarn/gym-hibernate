package com.epam.service;

import com.epam.entity.User;
import com.epam.repository.UserRepository;
import com.epam.util.UsernamePasswordUtil;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UsernamePasswordUtil usernamePasswordUtil;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, UsernamePasswordUtil usernamePasswordUtil) {
        this.userRepository = userRepository;
        this.usernamePasswordUtil = usernamePasswordUtil;
    }

    @Transactional
    public void createUser(User user) {
        String newUsername = usernamePasswordUtil.generateUsername(user.getFirstName(), user.getLastName());
        String newPassword = usernamePasswordUtil.generatePassword();
        User updated = user.toBuilder()
                        .username(newUsername)
                        .password(newPassword)
                        .build();

        log.info("Creating user: {}", updated.getUsername());
        try {
            userRepository.save(updated);
            log.debug("User saved with ID: {}", user.getId());
        } catch (Exception e) {
            log.error("Failed to save user: {}", e.getMessage(), e);
        }
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Transactional
    public boolean authenticate(String username, String password) {
        log.info("Authenticating user: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        // Check if user exists, is active, and password matches
        if (userOpt.isPresent() && userOpt.get().isActive()) {
            User user = userOpt.get();
            // Use the utility to check the raw password against the stored hash
            boolean matches = usernamePasswordUtil.checkPassword(password, user.getPassword());
            if(matches) {
                log.info("Authentication successful for user: {}", username);
                return true;
            } else {
                log.warn("Authentication failed for user: {} - Incorrect password", username);
                return false; // Password mismatch
            }
        }
        log.warn("Authentication failed for user: {} - User not found or inactive", username);
        return false; // User not found or not active
    }

//    @Transactional // This method modifies data, so needs a read-write transaction
//    public void changePassword(String username, String oldPassword, String newPassword) {
//        log.info("Attempting to change password for user: {}", username);
//        // Step 1: Authenticate the user with the old password
//        if (!authenticate(username, oldPassword)) {
//            log.error("Password change failed for {}: Authentication failed (old password incorrect or user inactive/not found).", username);
//            throw new SecurityException("Authentication failed for password change.");
//        }
//
//        // Step 2: Fetch the user (should exist if authentication passed)
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> {
//                    log.error("Password change failed: User {} not found after successful authentication.", username); // Should not happen
//                    return new NoResultException("User not found: " + username);
//                });
//
//        // Step 3: Validate the new password (e.g., length, complexity - add rules if needed)
//        if (newPassword == null || newPassword.isBlank()) {
//            log.error("Password change failed for {}: New password cannot be empty.", username);
//            throw new IllegalArgumentException("New password cannot be empty.");
//        }
//
//        // Step 4: Hash the new password and update the user
//        user.with(builder -> builder.password(passwordUtil.hashPassword(newPassword)));
//
//        userRepository.save(user); // Persist the change
//        log.info("Password changed successfully for user: {}", username);
//    }

    @Transactional
    public void setActiveStatus(String username, boolean isActive) {
        log.info("Setting active status for user {} to {}", username, isActive);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Failed to set active status: User {} not found.", username);
                    return new NoResultException("User not found: " + username);
                });
        User updated = user.toBuilder()
                        .isActive(isActive)
                                .build();
//        user.setActive(isActive);
//        user.with(builder -> builder.isActive(isActive));
        // Persist the change
        userRepository.save(updated);
        log.info("Active status updated successfully for user: {}", username);
    }
}

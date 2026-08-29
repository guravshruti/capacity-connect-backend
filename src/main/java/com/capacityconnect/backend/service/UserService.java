package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.User;
import com.capacityconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String signup(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            return "Name is required";
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            return "Valid email is required";
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            return "Password must be at least 6 characters";
        }
        // Check if email is already registered
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already registered";
        }

        // Secure the password before saving (never save plain text passwords)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("TRAINEE");
        }
        // New users start as pending approval
        user.setApprovalStatus("PENDING");
        userRepository.save(user);
        return "Signup successful";
    }
    public String updateApprovalStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!status.equals("APPROVED") && !status.equals("REJECTED")) {
            return "Invalid status. Use APPROVED or REJECTED";
        }

        user.setApprovalStatus(status);
        userRepository.save(user);
        return "User " + status.toLowerCase();
    }

    public String login(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        return "Login successful";
                    } else {
                        return "Invalid password";
                    }
                })
                .orElse("User not found");
    }
}
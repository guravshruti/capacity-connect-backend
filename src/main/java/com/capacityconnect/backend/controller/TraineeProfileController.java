package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.TraineeProfile;
import com.capacityconnect.backend.service.TraineeProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainee-profile")
public class TraineeProfileController {

    @Autowired
    private TraineeProfileService traineeProfileService;

    // Create or update a trainee's profile
    @PostMapping("/{userId}")
    public TraineeProfile saveOrUpdateProfile(@PathVariable Long userId, @RequestBody TraineeProfile profileData) {
        return traineeProfileService.saveOrUpdateProfile(userId, profileData);
    }

    // Get a trainee's profile by user id
    @GetMapping("/{userId}")
    public TraineeProfile getProfile(@PathVariable Long userId) {
        return traineeProfileService.getProfileByUserId(userId);
    }
}
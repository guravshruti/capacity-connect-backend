package com.capacityconnect.backend.service;

import com.capacityconnect.backend.model.TraineeProfile;
import com.capacityconnect.backend.model.User;
import com.capacityconnect.backend.repository.TraineeProfileRepository;
import com.capacityconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeProfileService {

    @Autowired
    private TraineeProfileRepository traineeProfileRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new profile, or update the existing one, for a given user
    public TraineeProfile saveOrUpdateProfile(Long userId, TraineeProfile profileData) {

        Optional<TraineeProfile> existingProfileOpt = traineeProfileRepository.findByUserId(userId);

        if (existingProfileOpt.isPresent()) {
            // Profile already exists -> update it
            TraineeProfile existingProfile = existingProfileOpt.get();
            existingProfile.setQualifications(profileData.getQualifications());
            existingProfile.setWorkExperience(profileData.getWorkExperience());
            existingProfile.setInterests(profileData.getInterests());
            existingProfile.setSkills(profileData.getSkills());
            existingProfile.setCertificates(profileData.getCertificates());
            return traineeProfileRepository.save(existingProfile);
        } else {
            // No profile yet -> create a new one, linked to the user
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            profileData.setUser(user);
            return traineeProfileRepository.save(profileData);
        }
    }

    // Fetch a profile by user id
    public TraineeProfile getProfileByUserId(Long userId) {
        return traineeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user id: " + userId));
    }
}
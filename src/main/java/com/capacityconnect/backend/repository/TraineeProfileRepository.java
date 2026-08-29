package com.capacityconnect.backend.repository;

import com.capacityconnect.backend.model.TraineeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeProfileRepository extends JpaRepository<TraineeProfile, Long> {

    Optional<TraineeProfile> findByUserId(Long userId);
}

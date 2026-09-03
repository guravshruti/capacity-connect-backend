package com.capacityconnect.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    // NEW FIELDS BELOW

    // "Technical", "Scientific", "Administrative", "Policy & Compliance"
    private String category;

    // "Beginner", "Intermediate", "Advanced", "All Levels"
    private String level;

    // Duration in hours, e.g. 4.0, 2.5, 8.0, 1.0
    private Double durationHours;

    // URL to a themed image for this course's card
    private String imageUrl;

    // Drives which badge + button shows on the card:
    // "REQUIRED", "MANDATORY", "COMPLETED", "NORMAL"
    private String status;

    // Only used when status = "COMPLETED", e.g. "2023"
    private String completedYear;
}
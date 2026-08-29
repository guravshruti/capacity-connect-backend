package com.capacityconnect.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "materials")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url; // the link to the file

    @ManyToOne
    @JoinColumn(name = "course_id") // this creates the "which course" link
    private Course course;

    // --- Constructors ---
    public Material() {
    }

    public Material(String title, String url, Course course) {
        this.title = title;
        this.url = url;
        this.course = course;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
package com.capacityconnect.backend.model;

public class AnalyticsSummary {

    private long totalUsers;
    private long totalCourses;
    private String mostPopularCourse;

    public AnalyticsSummary() {
    }

    public AnalyticsSummary(long totalUsers, long totalCourses, String mostPopularCourse) {
        this.totalUsers = totalUsers;
        this.totalCourses = totalCourses;
        this.mostPopularCourse = mostPopularCourse;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public String getMostPopularCourse() {
        return mostPopularCourse;
    }

    public void setMostPopularCourse(String mostPopularCourse) {
        this.mostPopularCourse = mostPopularCourse;
    }
}
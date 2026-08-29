package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.AnalyticsSummary;
import com.capacityconnect.backend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/summary")
    public AnalyticsSummary getSummary() {
        return analyticsService.getSummary();
    }
}
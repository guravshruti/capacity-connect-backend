package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestParam String role) {
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can view dashboard stats");
        }
        return dashboardService.getStats();
    }
}
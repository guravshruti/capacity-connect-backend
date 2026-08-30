package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Announcement;
import com.capacityconnect.backend.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;
    @CrossOrigin(origins="*")
    @PostMapping("/add")
    public Announcement addAnnouncement(@RequestBody Announcement announcement, @RequestParam String role) {
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can post announcements");
        }
        return announcementService.addAnnouncement(announcement);
    }

    @GetMapping("/all")
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }
}
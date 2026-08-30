package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.Certificate;
import com.capacityconnect.backend.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    // Generate a certificate for a completed enrollment
    @PostMapping("/generate/{enrollmentId}")
    public Certificate generateCertificate(@PathVariable Long enrollmentId) {
        return certificateService.generateCertificate(enrollmentId);
    }

    // View all certificates earned by a user
    @GetMapping("/user/{userId}")
    public List<Certificate> getCertificatesForUser(@PathVariable Long userId) {
        return certificateService.getCertificatesForUser(userId);
    }
}

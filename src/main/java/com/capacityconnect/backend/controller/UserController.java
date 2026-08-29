package com.capacityconnect.backend.controller;

import com.capacityconnect.backend.model.User;
import com.capacityconnect.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        return userService.signup(user);
    }
    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user.getEmail(), user.getPassword());
    }
    @PutMapping("/{userId}/approval")
    public String updateApprovalStatus(@PathVariable Long userId, @RequestParam String status, @RequestParam String role) {
        if (!role.equals("ADMIN")) {
            throw new RuntimeException("Access denied: only ADMIN can approve users");
        }
        return userService.updateApprovalStatus(userId, status);
    }
}

package com.example.developerteam.controller;

import com.example.developerteam.entity.User;
import com.example.developerteam.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // DTO для реєстрації
    public static class RegisterRequest {
        public String username;
        public String email;
        public String password;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest req) {
        try {
            User newUser = userService.registerUser(req.username, req.email, req.password);
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "username", newUser.getUsername()
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", ex.getMessage()));
        }
    }


    // @PostMapping("/login") — налаштовано в JwtAuthenticationFilter

    @GetMapping("/user-info")
    public ResponseEntity<?> userInfo() {
        // у запиті має бути JWT: "Authorization: Bearer <token>"
        // цю інформацію доставить JwtAuthorizationFilter
        return ResponseEntity.ok(Map.of("status", "authenticated"));
    }
}

package dev.priyanshu.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String password = payload.get("password");
        String email = payload.get("email");

        if (authService.registerUser(userId, password, email)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User ID already exists.");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String password = payload.get("password");

        if (authService.authenticateUser(userId, password)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful.");
            response.put("userId", userId);
            response.put("token", JwtUtil.generateToken(userId)); // Generate JWT token
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid credentials.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


}

package dev.priyanshu.movies.controller;

import dev.priyanshu.movies.JwtUtil;
import dev.priyanshu.movies.User;
import dev.priyanshu.movies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        boolean isRegistered = userService.registerUser(user);

        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.badRequest().body("User registration failed. User ID might already exist.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String password = payload.get("password");

        boolean isAuthenticated = userService.authenticateUser(userId, password);

        if (isAuthenticated) {
            String token = JwtUtil.generateToken(userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("userId", userId);
            response.put("token", token);  // Send JWT token to frontend
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Invalid credentials"));
        }
    }

    @PostMapping("/{userId}/watchlist")
    public ResponseEntity<String> addMovieToWatchlist(
            @PathVariable String userId,
            @RequestBody Map<String, String> payload,
            @RequestHeader("Authorization") String token) {

        if (!JwtUtil.validateToken(token.replace("Bearer ", ""), userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String movieId = payload.get("movieId");
        boolean result = userService.addMovieToWatchlist(userId, movieId);

        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or Movie already in watchlist.");
        }
        return ResponseEntity.ok("Movie added to watchlist.");
    }

    @GetMapping("/{userId}/watchlist")
    public ResponseEntity<User> getUserWatchlist(@PathVariable String userId, @RequestHeader("Authorization") String token) {
        if (!JwtUtil.validateToken(token.replace("Bearer ", ""), userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        User user = userService.getUserWatchlist(userId);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{userId}/watchlist/{movieId}")
    public ResponseEntity<User> removeMovieFromWatchlist(
            @PathVariable String userId,
            @PathVariable String movieId,
            @RequestHeader("Authorization") String token) {

        if (!JwtUtil.validateToken(token.replace("Bearer ", ""), userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return new ResponseEntity<>(userService.removeMovieFromWatchlist(userId, movieId), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/watchlist")
    public ResponseEntity<String> clearWatchlist(
            @PathVariable String userId,
            @RequestHeader("Authorization") String token) {

        if (!JwtUtil.validateToken(token.replace("Bearer ", ""), userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.clearUserWatchlist(userId);
        return ResponseEntity.ok("Watchlist cleared successfully");
    }
}

package dev.priyanshu.movies.service;

import dev.priyanshu.movies.User;
import dev.priyanshu.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean addMovieToWatchlist(String userId, String movieId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();
        if (user.getWatchList().contains(movieId)) {
            throw new RuntimeException("Movie already in watchlist");
        }
        user.getWatchList().add(movieId);
        userRepository.save(user);
        return true;
    }

    public User getUserWatchlist(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User removeMovieFromWatchlist(String userId, String movieId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();
        user.getWatchList().remove(movieId);
        userRepository.save(user);
        return user;
    }

    public void clearUserWatchlist(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setWatchList(new ArrayList<>());
        userRepository.save(user);
    }

    public boolean registerUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Hash password before saving
        userRepository.save(user);
        return true;
    }

    public boolean authenticateUser(String userId, String password) {
        System.out.println("🔹 Authenticating user: " + userId);

        Optional<User> user = userRepository.findByUserId(userId);
        if (user.isPresent()) {
            System.out.println("✅ User found in database.");
            System.out.println("🔹 Stored password: " + user.get().getPassword());
            System.out.println("🔹 Provided password: " + password);

            if (user.get().getPassword().equals(password)) {
                System.out.println("✅ Authentication successful.");
                return true;
            } else {
                System.out.println("❌ Password mismatch.");
            }
        } else {
            System.out.println("❌ User not found in database.");
        }

        return false;
    }

}

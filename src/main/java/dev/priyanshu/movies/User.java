package dev.priyanshu.movies;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {
    private String id; // Primary key
    @Id
    private String userId; // Chosen user ID for login
    private String password; // User's password (hashed)
    private String email; // Email address
    private List<String> watchList = new ArrayList<>(); // User's watchlist




    public User(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.watchList = new ArrayList<>(); // Initialize watchlist
    }

    public User() {}
    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getWatchList() {
        return watchList;
    }

    public void setWatchList(List<String> watchList) {
        this.watchList = watchList;
    }
}

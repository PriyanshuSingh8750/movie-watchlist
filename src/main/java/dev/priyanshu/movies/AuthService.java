package dev.priyanshu.movies;
import dev.priyanshu.movies.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // ✅ Use BCrypt

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registerUser(String userId, String password, String email) {
        if (userRepository.existsById(userId)) {
            return false; // User ID already exists
        }

        // ✅ Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User(userId, hashedPassword, email);
        userRepository.save(newUser);

        return true; // Registration successful
    }


    public boolean authenticateUser(String userId, String password) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String storedHashedPassword = user.getPassword(); // Get hashed password from DB

            System.out.println("🔹 Authenticating user: " + userId);
            System.out.println("✅ User found in database.");
            System.out.println("🔹 Stored password (hashed): " + storedHashedPassword);
            System.out.println("🔹 Provided password: " + password);

            // ✅ Properly compare raw password with hashed password
            if (BCrypt.checkpw(password, storedHashedPassword)) {
                System.out.println("✅ Authentication successful.");
                return true;
            } else {
                System.out.println("❌ Password mismatch.");
            }
        } else {
            System.out.println("❌ User not found.");
        }

        return false;
    }


}

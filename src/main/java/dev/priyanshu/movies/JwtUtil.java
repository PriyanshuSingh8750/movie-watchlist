package dev.priyanshu.movies;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "your_super_secret_key_32chars_long";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    // Generate JWT Token
    public static String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .sign(ALGORITHM);
    }

    // Validate and Decode JWT Token
    public static DecodedJWT verifyToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        return verifier.verify(token);
    }

    // Extract User ID from JWT
    public static String extractUserId(String token) {
        try {
            return verifyToken(token).getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    // Validate Token
    public static boolean validateToken(String token, String userId) {
        String extractedUserId = extractUserId(token);
        return extractedUserId != null && extractedUserId.equals(userId);
    }
}

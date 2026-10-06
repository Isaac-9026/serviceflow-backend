package com.serviceflow;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HashTest {
    @Test
    void testHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("password123");
        System.out.println("HASH_GENERATED: " + hash);
        boolean match = encoder.matches("password123", "$2a$10$wT/t/X45/E1/qKx1YxT.oO5J92j7t3xJ5r1t925q29Xv7O5W3u1hS");
        System.out.println("MATCH_OLD_HASH: " + match);
        assertTrue(match, "Old hash does not match password123");
    }
}

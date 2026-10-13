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
        boolean match = encoder.matches("password123", "$2a$10$VwmHk9lm4GuRlcJMUG.DkeCTbeeLj7LJKIiAzRy3bw.hQUFb7c6.C");
        assertTrue(match, "El hash de V4 debe coincidir con password123");
    }
}

package com.okcapital.shared;

import java.security.SecureRandom;
import java.util.UUID;

public final class UuidV8Generator {
    private static final SecureRandom RANDOM = new SecureRandom();

    private UuidV8Generator() {
    }

    public static UUID generate() {
        long mostSignificantBits = RANDOM.nextLong();
        long leastSignificantBits = RANDOM.nextLong();

        mostSignificantBits = (mostSignificantBits & 0xffffffffffff0fffL) | 0x0000000000008000L;
        leastSignificantBits = (leastSignificantBits & 0x3fffffffffffffffL) | 0x8000000000000000L;

        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
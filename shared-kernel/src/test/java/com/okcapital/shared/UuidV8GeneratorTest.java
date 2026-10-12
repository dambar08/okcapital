package com.okcapital.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UuidV8GeneratorTest {

    @Test
    void generatesRfc9562VersionEightUuid() {
        var uuid = UuidV8Generator.generate();

        assertEquals(8, uuid.version());
        assertEquals(2, uuid.variant());
    }
}
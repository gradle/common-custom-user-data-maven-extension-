package com.gradle.ccud.adapters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DevelocityApiAdapterTest {

    @Test
    @DisplayName("adapter cannot be created on arbitrary objects")
    void testInvalidExtensionObject() {
        // when
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> DevelocityAdapter.create(new Object())
        );

        // then
        assertEquals("Provided extension of type 'java.lang.Object' is neither the Develocity configuration nor the Gradle Enterprise extension", thrown.getMessage());
    }
}

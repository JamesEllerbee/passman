package com.jamesellerbee.utilities.properties;

import com.jamesellerbee.interfaces.IPropertyProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests PropertyProvider
 */
@DisplayName("A PropertyProvider should...")
class PropertyProviderTest
{
    /**
     * Verifies that PropertyProvider can store then get a
     */
    @Test
    @DisplayName("store a value with a key and get that same value.")
    void testStoreThenGet()
    {
        // Given a PropertyProvider
        IPropertyProvider propertyProvider = new PropertyProvider();

        // Given a test key and a test value
        String testKey = "testKey";
        String testValue = "testValue";

        // When storing a property
        propertyProvider.store(testKey, testValue);

        // Then when retrieving the property, it matches the test value.
        String retrievedPropertyValued = propertyProvider.get(testKey, "");

        assertEquals(testValue, retrievedPropertyValued, "Expected values to be equal.");
    }
}
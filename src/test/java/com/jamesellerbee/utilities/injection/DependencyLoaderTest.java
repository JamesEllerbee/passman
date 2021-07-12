package com.jamesellerbee.utilities.injection;

import com.jamesellerbee.interfaces.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests dependency loader
 */
@DisplayName("A DependencyLoader should...")
class DependencyLoaderTest
{
    private IInjector testInjector;

    @BeforeEach
    void setup()
    {
        testInjector = Injector.getNewInstance();
    }

    @AfterEach
    void tearDown()
    {
        testInjector = null;
    }

    /**
     * Verifies that test injector contains all the expected loaded dependencies.
     */
    @Test
    @DisplayName("load and contain dependencies.")
    void testLoad()
    {
        // Given test injector

        // Given a dependency loader
        DependencyLoader dependencyLoader = new DependencyLoader(testInjector);

        // When loading dependencies.
        dependencyLoader.load();

        // Then test injector contains all the expected dependencies.
        assertTrue(testInjector.contains(IPropertyProvider.class), "Expected test injector to contain a PropertyProvider.");
        assertTrue(testInjector.contains(ILoginInfoProvider.class), "Expected test injector to contain a LoginInfoProvider.");
        assertTrue(testInjector.contains(IConsoleHandler.class), "Expected test injector to contain a ConsoleHandler.");
        assertTrue(testInjector.contains(IEncryptionEngine.class), "Expected test injector to contain an EncryptionEngine.");
    }
}
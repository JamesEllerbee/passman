package com.jamesellerbee.utilities.injection;

import com.jamesellerbee.interfaces.IInjector;
import com.jamesellerbee.interfaces.ILogger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertSame;

@DisplayName("An Injector should...")
class InjectorTest
{
    IInjector testInjector;

    @BeforeEach
    void setUp()
    {
        testInjector = Injector.getNewInstance();
    }

    @AfterEach
    void tearDown()
    {
        testInjector = null;
    }

    /**
     * Verifies that Injector returns the same singleton reference.
     */
    @Test
    @DisplayName("return the same singleton reference")
    void testGetSameInstance()
    {
        assertSame(Injector.getInstance(), Injector.getInstance(), "Expected the same instance.");
    }

    @Test
    @DisplayName("be able to register an instance then resolve that instance.")
    void testRegisterThenResolve()
    {
        // Given test injector from setup

        // Given a logger to register in the injector
        ILogger mockLogger = strictMock(ILogger.class);
        replay(mockLogger);

        // When registering the mock logger
        testInjector.register(ILogger.class, mockLogger);

        // Then when resolving the logger, it is the same reference
        ILogger resolvedLogger = testInjector.resolve(ILogger.class);
        assertSame(mockLogger, resolvedLogger, "Expected to get same instance from injector.");

        // Verify mock
        verify(mockLogger);
    }

    @Test
    @DisplayName("be able to register an instance with a key then resolve that instance.")
    void testRegisterWithKeyThenResolve()
    {
        // Given test injector from setup

        // Given a logger to register in the injector
        ILogger mockLogger = strictMock(ILogger.class);
        replay(mockLogger);

        // Given an object to map the logger to
        String key = "logger # 1";

        // When registering the mock logger with the key
        testInjector.register(ILogger.class, key, mockLogger);

        // Then when resolving the logger with the key, it is the same reference.
        ILogger resolvedLogger = testInjector.resolve(ILogger.class, key);
        assertSame(mockLogger, resolvedLogger, "Expected to get same instance from injector");
    }
}
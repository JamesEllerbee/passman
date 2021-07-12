package com.jamesellerbee.utilities.injection;

import com.jamesellerbee.data.LoginInfoHandler;
import com.jamesellerbee.data.LoginInfoProvider;
import com.jamesellerbee.interfaces.*;
import com.jamesellerbee.security.EncryptionEngine;
import com.jamesellerbee.utilities.console.ConsoleHandler;
import com.jamesellerbee.utilities.logging.SimpleLogger;
import com.jamesellerbee.utilities.properties.PropertyProvider;

/**
 * Instantiates and loads dependencies into the injector.
 */
public class DependencyLoader
{
    private final ILogger logger = new SimpleLogger(getClass().getName());

    private final IInjector dependencyInjector;

    public DependencyLoader(IInjector dependencyInjector)
    {
        if (dependencyInjector == null)
        {
            logger.error("Dependency injector can not be null.");
            throw new IllegalArgumentException();
        }

        this.dependencyInjector = dependencyInjector;
        // Immediately load a property provider so it is available when loading other dependencies.
        dependencyInjector.register(IPropertyProvider.class, new PropertyProvider());
    }

    public void load()
    {
        loadProviders();
        loadHandlers();
        loadEngines();
    }

    private void loadHandlers()
    {
        dependencyInjector.register(IConsoleHandler.class, new ConsoleHandler());
        dependencyInjector.register(ILoginInfoHandler.class, new LoginInfoHandler(dependencyInjector));
    }

    private void loadProviders()
    {
        dependencyInjector.register(ILoginInfoProvider.class, new LoginInfoProvider(dependencyInjector));
    }

    private void loadEngines()
    {
        dependencyInjector.register(IEncryptionEngine.class, new EncryptionEngine());
    }
}

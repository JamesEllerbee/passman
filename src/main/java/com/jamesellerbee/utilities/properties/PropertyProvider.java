package com.jamesellerbee.utilities.properties;

import com.jamesellerbee.interfaces.ILogger;
import com.jamesellerbee.interfaces.IPropertyProvider;
import com.jamesellerbee.utilities.logging.SimpleLogger;

import java.io.*;
import java.security.cert.CertSelector;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Queries a specified properties file and returns content stored in it.
 */
public class PropertyProvider implements IPropertyProvider
{
    private final ILogger logger = new SimpleLogger(getClass().getName());
    private static String DEFAULT_PATH = System.getProperty("user.dir") + System.getProperty("file.separator") + "passman.properties";

    private Properties properties;

    public PropertyProvider()
    {
        this(DEFAULT_PATH);
    }

    public PropertyProvider(String propertiesPath)
    {
        File propertiesFile = new File(propertiesPath);
        properties = new Properties();
        try (InputStream propertiesInputStream = new FileInputStream(propertiesFile))
        {
            properties.load(propertiesInputStream);
            logger.info("Application Properties loaded.");

        } catch (IOException e)
        {
            logger.error("There was a problem reading the application properties.");
            logger.debug("Exception: " + e.getMessage());
        }
    }

    @Override
    public <T> T get(String key, T defaultValue)
    {
        T result = defaultValue;

        String value = (String) properties.get(key);
        if(value != null)
        {
            result = cast(value, defaultValue);
        }

        return result;
    }

    @Override
    public void store(String key, String value)
    {
        logger.warn("Unsupported method call.");
    }

    private <T> T cast(String value, T defaultValue)
    {
        T result = defaultValue;

        if (defaultValue instanceof String)
        {
            result = (T) value;
        }
        else
        {
            logger.warn("Unsupported type.");
        }

        return result;
    }
}

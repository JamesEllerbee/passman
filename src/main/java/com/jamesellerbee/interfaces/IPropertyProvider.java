package com.jamesellerbee.interfaces;

/**
 * Provides an interface for a property provider entity.
 */
public interface IPropertyProvider
{
    /**
     * Queries the properties file for the key.
     *
     * <p>
     * Tries to get the key from the properties file, and returns the type of the default value.
     * </p>
     *
     * @param key The property key to query from the properties file.
     * @param defaultValue The default value if the key does not exist or cannot be casted.
     * @param <T> The type of the value to retrieve.
     * @return The value mapped to the key or the default value if it does not exist or can not be casted to the type.
     */
    <T> T get(String key, T defaultValue);

    /**
     * Stores the value with the key in the properties file.
     * @param key The key.
     * @param value The value to store to the key.
     */
    void store(String key, String value);
}

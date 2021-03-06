package com.jamesellerbee.interfaces;

/**
 * Provides an interface for dependency injection.
 */
public interface IInjector
{
    /**
     * Register an object to the injector.
     *
     * @param type  The type of the object
     * @param value The instance of the object.
     * @param <T>   The type.
     */
    <T> void register(Class<T> type, T value);

    /**
     * Register an object to the injector to map to a key.
     *
     * <p>
     * This would allow more than one of the same type of object to be registered.
     * </p>
     *
     * @param type  The type of the object
     * @param key   The key to map the value to.
     * @param value The instance of the object.
     * @param <T>   The type of the object.
     */
    <T> void register(Class<T> type, Object key, T value);

    /**
     * Resolve an object from the injector.
     *
     * @param type The type to resolve.
     * @param <T>  The type of the object.
     * @return The instance of the object relating of the type specified, null if it does not exist.
     */
    <T> T resolve(Class<T> type);

    /**
     * Resolve an object mapped to the key from the injector.
     *
     * @param type The type to resolve.
     * @param key  The key the object is mapped to.
     * @param <T>  The type of the object.
     * @return The instance of the object mapped to the keep of the type specified, null if it does not exist.
     */
    <T> T resolve(Class<T> type, Object key);

    /**
     * Check if injector contains the type
     *
     * @param type The class.
     * @param <T>  The type of the class.
     * @return True if the injector has the class, otherwise false.
     */
    <T> boolean contains(Class<T> type);

    /**
     * Check if injector contains the type with the key.
     *
     * @param type The class.
     * @param key  The key the class is mapped to.
     * @param <T>  The type of the class.
     * @return Tru if the injector has the class mapped to the key, otherwise false.
     */
    <T> boolean contains(Class<T> type, Object key);
}

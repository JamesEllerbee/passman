package com.jamesellerbee.utilities.injection;

import com.jamesellerbee.interfaces.IInjector;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Injector implements IInjector
{
    private static IInjector instance;

    private final Map<Class<?>, Object> dependencyMap;
    private final Map<Class<?>, Map<Object, Object>> dependencyMapWithKey;

    /**
     * Get singleton reference to an injector.
     * @return The injector reference.
     */
    public static IInjector getInstance()
    {
        if(instance == null)
        {
            instance = new Injector();
        }

        return instance;
    }

    /**
     * Get a new instance of an injector.
     * @return The injector reference.
     */
    public static IInjector getNewInstance()
    {
        return new Injector();
    }

    private Injector()
    {
        dependencyMap = new ConcurrentHashMap<>();
        dependencyMapWithKey = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void register(Class<T> type, T value)
    {
        if(!dependencyMap.containsKey(type))
        {
            dependencyMap.put(type, value);
        }
    }

    @Override
    public <T> void register(Class<T> type, Object key, T value)
    {
        Map<Object, Object> dependencyMap;
        if(dependencyMapWithKey.containsKey(type))
        {
            dependencyMap = dependencyMapWithKey.get(type);
        }
        else
        {
            dependencyMap = new ConcurrentHashMap<>();
            dependencyMapWithKey.put(type, dependencyMap);
        }
        dependencyMap.put(key, value);
    }

    @Override
    public <T> T resolve(Class<T> type)
    {
        return (T) dependencyMap.get(type);
    }

    @Override
    public <T> T resolve(Class<T> type, Object key)
    {
        return (T) dependencyMapWithKey.get(type).get(key);
    }
}

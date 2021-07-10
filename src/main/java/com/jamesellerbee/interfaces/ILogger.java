package com.jamesellerbee.interfaces;

/**
 * Provides an interface for a logger.
 */
public interface ILogger
{
    void verbose(String message);

    void debug(String message);

    void warn(String message);

    void info(String message);

    /**
     * Logs an error.
     * @param message The content to output.
     */
    void error(String message);
}

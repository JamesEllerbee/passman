package com.jamesellerbee.utilities.logging;

import com.jamesellerbee.interfaces.ILogger;

public class SimpleLogger implements ILogger
{

    private String location;

    public SimpleLogger(String location)
    {
        this.location = location;
    }

    @Override
    public void verbose(String message)
    {
        System.out.println("VERBOSE: [" + location + "] " + message);

    }

    @Override
    public void debug(String message)
    {
        System.out.println("DEBUG: [" + location + "] " + message);

    }

    public void warn(String message)
    {
        System.out.println("WARNING: [" + location + "] " + message);
    }

    @Override
    public void info(String message)
    {
        System.out.println("INFO: [" + location + "] " + message);
    }

    @Override
    public void error(String message)
    {
        System.err.println("E: [ " + location + " ] " + message);
    }
}

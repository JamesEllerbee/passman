package com.jamesellerbee.logging;

public class Logger {

    private String location;

    public Logger(String location)
    {
        this.location = location;
    }

    public void error(String message)
    {
        System.err.println("E: [ " + location + " ] " + message);
    }
}

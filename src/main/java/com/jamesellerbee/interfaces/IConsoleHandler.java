package com.jamesellerbee.interfaces;

import com.jamesellerbee.utilities.console.ConsoleException;

public interface IConsoleHandler
{
    /**
     * Utilizes the console to read a line from CLI.
     * @return The String read in from the console.
     */
    String readLine();

    /**
     * Utilizes the console, if able, to securely retrieve a password from CLI.
     * @return The password String read in from the console.
     */
    String readPassword() throws ConsoleException;
}

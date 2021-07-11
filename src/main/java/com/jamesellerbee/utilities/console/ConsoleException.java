package com.jamesellerbee.utilities.console;

/**
 * Excepts when a console cannot be created.
 */
public class ConsoleException extends Exception
{
  /**
   * Creates a Console Exception.
   */
  public ConsoleException()
  {
    super("Cannot create a console.");
  }
}

package com.jamesellerbee.utilities;

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
    super("Cannot created a console.");
  }
}

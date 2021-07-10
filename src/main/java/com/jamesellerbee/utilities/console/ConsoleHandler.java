package com.jamesellerbee.utilities.console;

import com.jamesellerbee.interfaces.IConsoleHandler;

import java.io.Console;
import java.util.Scanner;

public class ConsoleHandler implements IConsoleHandler
{
  Scanner scanner;

  public ConsoleHandler()
  {
    scanner = new Scanner(System.in);
  }

  public String readLine()
  {
    return scanner.nextLine();
  }

  public String readPassword() throws ConsoleException
  {
    Console console = System.console();
    if(console == null)
    {
      throw new ConsoleException();
    }

    return new String(console.readPassword());
  }
}

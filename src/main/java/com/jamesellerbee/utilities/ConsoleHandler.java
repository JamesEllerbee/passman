package com.jamesellerbee.utilities;

import java.io.Console;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleHandler
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

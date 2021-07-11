package com.jamesellerbee.data;

import com.jamesellerbee.interfaces.ILoginInfoHandler;
import com.jamesellerbee.ui.models.LoginInfo;
import com.jamesellerbee.utilities.console.ConsoleException;

import java.io.File;
import java.util.Scanner;

/**
 * Handles writing and removing login info from system.
 */
public class LoginInfoHandler implements ILoginInfoHandler
{
    @Override
    public void store(LoginInfo loginInfo)
    {

    }

    @Override
    public void remove(String identifier)
    {

    }
//    public String processCommand(String command, String[] args)
//    {
//        String output = "";
//        switch (command)
//        {
//            case "store":
//                output = store();
//                break;
//            case "get":
//                output = retrieve(args[1]);
//                break;
//            case "ls":
//            case "list":
//                output = loginInfoProvider.retrieveAllAsString(path);
//                break;
//            case "rm":
//                output = remove(args[1]);
//                break;
//            case "help":
//                output = "store: writes a entry\nget <identifier>: outputs contents of store.";
//                break;
//            default:
//                output = "Unrecognized command";
//                break;
//        }
//        return output;
//    }
//
//    private String remove(String arg)
//    {
//        String output = "";
//        File toRemove = new File(path + arg + CONTENT_FILE_EXTENSION);
//        if (toRemove.exists())
//        {
//            if (toRemove.delete())
//            {
//                output = "Done.";
//            } else
//            {
//                System.err.println("Could not delete that id.");
//            }
//        } else
//        {
//            System.err.println("That id does not exist.");
//        }
//
//        return output;
//    }
//
//    private String store()
//    {
//        String output;
//
//        System.out.print("Enter id: ");
//        String name = consoleHandler.readLine();
//
//        System.out.print("Enter user: ");
//        name += "_" + consoleHandler.readLine();
//        String content = "";
//        try
//        {
//            System.out.print("Enter pass: ");
//            content += consoleHandler.readPassword();
//        } catch (ConsoleException e)
//        {
//            logger.error("Cannot securely read password.");
//            System.out.print("Do you want to input your password in plain text? (y/n): ");
//            Scanner scanner = new Scanner(System.in);
//            String response = scanner.nextLine();
//            if(response.equalsIgnoreCase("y"))
//            {
//                System.out.print("password: ");
//                content += scanner.nextLine();
//            }
//            else
//            {
//                content += "NOT SET";
//            }
//
//        }
//        finally
//        {
//            storeContent(content, name);
//            output = "Done.";
//        }
//
//        return output;
//    }
//
//    public void storeContent(String content, String fileName)
//    {
//        encryptionEngine.encrypt(content, this.path + SYSTEM_FILE_SEPARATOR + fileName + CONTENT_FILE_EXTENSION);
//    }
//
//    public String retrieve(String path)
//    {
//        return encryptionEngine.decrypt(this.path + path + CONTENT_FILE_EXTENSION);
//    }
}

package com.jamesellerbee.utilities.files;

import java.io.File;

public class FileUtility
{
    /**
     * Creates a directory if it does not already exist.
     * @param path The directory path.
     */
    public static void createDirectory(String path)
    {
        File identifiersPath = new File(path);
        if (identifiersPath.mkdir())
        {
            System.out.println("Directory created.");
        }
    }
}

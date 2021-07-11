package com.jamesellerbee.interfaces;

import com.jamesellerbee.ui.models.LoginInfo;

import java.util.List;

/**
 * Provides an interface for login info provider.
 */
public interface ILoginInfoProvider
{
    /**
     * Gets info from UI and persists login data.
     */
    void createNewLoginInfo();

    /**
     * Retrieves all login info from file system.
     *
     * @param path The path to read in login data.
     * @return List of login information.
     */
    List<LoginInfo> getAllLoginInfo(String path);

    /**
     * Retrieves all login info from file system formatted as a string.
     *
     * @param path The path to read in login data.
     * @return String containing login data.
     */
    String retrieveAllAsString(String path);
}

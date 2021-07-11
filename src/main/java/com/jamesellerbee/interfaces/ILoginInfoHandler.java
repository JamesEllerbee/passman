package com.jamesellerbee.interfaces;

import com.jamesellerbee.ui.models.LoginInfo;

/**
 * Provides an interface for a Login Info Handler.
 */
public interface ILoginInfoHandler
{
    /**
     * Stores the login info to the system.
     * @param loginInfo The login info.
     */
    void store(LoginInfo loginInfo);
    /**
     * Deletes specified identifier from system.
     * @param identifier The identifier of the login info.
     */
    void remove(String identifier);
}

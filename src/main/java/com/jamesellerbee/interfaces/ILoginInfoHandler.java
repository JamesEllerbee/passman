package com.jamesellerbee.interfaces;

import com.jamesellerbee.ui.models.LoginInfo;

/**
 * Provides an interface for a Login Info Handler.
 */
public interface ILoginInfoHandler
{
    /**
     * Stores the login info to the system.
     *
     * @param loginInfo The login info.
     * @param newContent If the loginInfo is new.
     * @return True if stored login created, otherwise false.
     */
    boolean store(LoginInfo loginInfo, boolean newContent);

    /**
     * Deletes specified login info from system.
     *
     * @param loginInfo The login info.
     * @return True if stored login was deleted, otherwise false.
     */
    boolean remove(LoginInfo loginInfo);
}

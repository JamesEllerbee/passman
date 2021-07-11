package com.jamesellerbee.ui.models;

public class LoginInfo
{
    private final String identifier;
    private final String username;
    private final String password;

    public LoginInfo(String identifier, String username, String password)
    {
        this.identifier = identifier;
        this.username = username;
        this.password = password;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}

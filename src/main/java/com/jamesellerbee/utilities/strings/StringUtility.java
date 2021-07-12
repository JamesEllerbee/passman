package com.jamesellerbee.utilities.strings;

public class StringUtility
{
    public static final char BULLET = '\u2022';

    public static String getPasswordTemplate(String password)
    {
        StringBuilder hiddenPassword = new StringBuilder();
        for(int i = 0; i < password.length(); i++)
        {
            hiddenPassword.append(BULLET);
        }

        return hiddenPassword.toString();
    }
}

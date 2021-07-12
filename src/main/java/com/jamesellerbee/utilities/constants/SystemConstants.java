package com.jamesellerbee.utilities.constants;

/**
 * Constants related to system.
 *
 * <p>
 * e.g. file paths, and system properties.
 * </p>
 */
public class SystemConstants
{
    /**
     * The default directory path value.
     */
    public static final String DEFAULT_PATH = System.getProperty("user.dir");
    /**
     * The system file separator.
     */
    public static final String SYSTEM_FILE_SEPARATOR = System.getProperty("file.separator");
    /**
     * The system line separator.
     */
    public static final String SYSTEM_LINE_SEPARATOR = System.lineSeparator();


}

package me.vowed.api.utils;

import me.vowed.api.plugin.Vowed;

/**
 * Created by JPaul on 9/30/2015.
 */
public class Logger
{
    public static String logPrefix;
    private static boolean enabled;

    public static void initiate(String prefix)
    {
        logPrefix = prefix;
        enabled = true;
    }

    public static void log(Log.LogLevel logLevel, String message, boolean logToConsole)
    {
        if (enabled)
        {
            if (logToConsole)
            {
                switch (logLevel)
                {
                    case NORMAL:
                        Vowed.LOG.info(message);
                        break;

                    case WARNING:
                        Vowed.LOG.warning(message);
                        break;

                    case SEVERE:
                        Vowed.LOG.severe(message);
                        break;
                }
            }
        }
    }

    public static void log(Log.LogLevel logLevel, String message, Exception exception, boolean logToConsole)
    {
        if (enabled)
        {
            if (logToConsole)
            {
                Vowed.LOG.severe(message);
            }
            exception.printStackTrace();
        }
    }
}

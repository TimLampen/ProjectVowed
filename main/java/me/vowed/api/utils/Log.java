package me.vowed.api.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Basic logging to a server console
 * <p>
 * Supports chat colours :)
 */
public class Log
{

    private String prefix;
    private ConsoleCommandSender console;

    public Log(String prefix) {
        this.prefix = prefix;
    }

    public void log(String message) {
        info(message);
    }

    public void info(String message) {
        console(message);
    }

    public void info(boolean bool)
    {
        console(String.valueOf(bool));
    }

    public void info(int integer)
    {
        console(String.valueOf(integer));
    }

    public void warning(String message) {
        console(LogLevel.WARNING, message);
    }

    public void warning(boolean bool)
    {
        console(LogLevel.WARNING, String.valueOf(bool));
    }

    public void warning(int integer)
    {
        console(LogLevel.WARNING, String.valueOf(integer));
    }

    public void severe(String message) {
        console(LogLevel.SEVERE, message);
    }

    public void severe(boolean bool)
    {
        console(LogLevel.SEVERE, String.valueOf(bool));
    }

    public void severe(int integer)
    {
        console(LogLevel.SEVERE, String.valueOf(integer));
    }

    public void debug(String message) {
        console(LogLevel.DEBUG, message);
    }

    public void debug(boolean bool)
    {
        console(LogLevel.DEBUG, String.valueOf(bool));
    }

    public void debug(int integer)
    {
        console(LogLevel.DEBUG, String.valueOf(integer));
    }

    public void console(String message) {
        console(LogLevel.NORMAL, message);
    }

    public void console(LogLevel logLevel, String message) {
        if (console == null) {
            console = Bukkit.getConsoleSender();
        }
        console.sendMessage(logLevel.getPrefix() + message);
    }

    public enum LogLevel
    {
        NORMAL(ChatColor.WHITE + "{INFO} "),
        SEVERE(ChatColor.DARK_RED + "{SEVERE} " + ChatColor.WHITE),
        WARNING(ChatColor.RED + "{WARNING} " + ChatColor.WHITE),
        DEBUG(ChatColor.GREEN + "{DEBUG} " + ChatColor.WHITE);

        private String prefix;

        LogLevel(String prefix)
        {
            this.prefix = prefix;
        }

        public String getPrefix()
        {
            return Logger.logPrefix + " " + this.prefix;
        }
    }
}
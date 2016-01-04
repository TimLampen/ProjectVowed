package me.vowed.api.utils.bukkit;

import me.vowed.api.plugin.Vowed;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Created by JPaul on 1/2/2016.
 */
public class BukkitUtil
{
    public static void sendCommand(String command)
    {
        ConsoleCommandSender commandSender = Vowed.getPlugin().getServer().getConsoleSender();

        Vowed.getPlugin().getServer().dispatchCommand(commandSender, command);
    }
}

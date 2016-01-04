package me.vowed.api.utils;

import org.bukkit.ChatColor;

/**
 * Created by JPaul on 1/2/2016.
 */
public class Title
{
    public static String commandTitle(String name)
    {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "----- " + name + " -----";
    }
}

package me.vowed.api.utils.worldedit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Created by JPaul on 1/2/2016.
 */
public class WorldEditUtil
{
    public static WorldEditPlugin getWorldEdit()
    {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

        if (plugin instanceof WorldEditPlugin)
        {
            return (WorldEditPlugin) plugin;
        }

        return null;
    }
}

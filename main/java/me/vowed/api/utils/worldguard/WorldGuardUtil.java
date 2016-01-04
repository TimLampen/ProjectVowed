package me.vowed.api.utils.worldguard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.vowed.api.plugin.Vowed;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

/**
 * Created by JPaul on 1/2/2016.
 */
public class WorldGuardUtil
{
    public static int[] getCenter(ProtectedRegion region)
    {
        int maxX = region.getMaximumPoint().getBlockX();
        int minX = region.getMinimumPoint().getBlockX();
        int maxZ = region.getMaximumPoint().getBlockZ();
        int minZ = region.getMinimumPoint().getBlockZ();

        int centerX = (maxX + minX) / 2;
        int centerZ = (maxZ + minZ) / 2;

        return new int[]{centerX, centerZ};
    }

    public static boolean isOwner(Player player, ProtectedRegion region)
    {
        for (UUID uuid : region.getOwners().getUniqueIds())
        {
            if (player.getUniqueId().equals(uuid))
            {
                return true;
            }
        }

        return false;
    }

    public static ProtectedRegion getRegion(Location location)
    {
        ApplicableRegionSet regionSet = getWorldGuard().getRegionManager(location.getWorld()).getApplicableRegions(location);

        for (ProtectedRegion region : regionSet)
        {
            return region;
        }

        return null;
    }

    public static WorldGuardPlugin getWorldGuard()
    {
        Plugin plugin = Vowed.getPlugin().getServer().getPluginManager().getPlugin("WorldGuard");

        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
        {
            return null; // Maybe you want throw an exception instead
        }

        return (WorldGuardPlugin) plugin;
    }
}

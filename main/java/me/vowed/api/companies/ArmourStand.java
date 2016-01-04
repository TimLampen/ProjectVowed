package me.vowed.api.companies;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

/**
 * Created by JPaul on 12/31/2015.
 */
public class ArmourStand extends EntityArmorStand
{
    public ArmourStand(World world)
    {

        super(world);
    }

    public ArmourStand(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public ArmourStand(Location location)
    {
        super(((CraftWorld) location.getWorld()).getHandle(), location.getBlockX(), location.getY(), location.getBlockZ());
        setInvisible(true);
    }
}

package me.vowed.api.pets;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

/**
 * Created by JPaul on 11/29/2015.
 */
public abstract class EntityPet extends EntityCreature
{
    Pet pet;
    boolean isPet;

    public EntityPet(World world, Pet pet)
    {
        super(world);
        this.pet = pet;
    }

    public boolean canMove()
    {
        return true;
    }

    public boolean isPet()
    {
        return isPet;
    }

    public Pet getPet()
    {
        return pet;
    }

    public void setLocation(Location location)
    {
        this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); //entities location method
    }
}

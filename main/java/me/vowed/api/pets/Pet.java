package me.vowed.api.pets;

import javafx.geometry.VPos;
import me.vowed.api.pets.petTypes.PetWolf;
import me.vowed.api.pets.player.PetPlayer;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by JPaul on 11/29/2015.
 */
public abstract class Pet implements IPet
{
    EntityPet entityPet;
    Player petOwner;
    String name;
    UUID uuid;

    public Pet(Player owner)
    {
        this.petOwner = owner;
    }

    public EntityPet getEntityPet()
    {
        return this.entityPet;
    }

    @Override
    public Location getLocation()
    {
        return entityPet.getBukkitEntity().getLocation();
    }

    @Override
    public Location getBlockLocation()
    {
        return new Location(entityPet.getBukkitEntity().getWorld(), entityPet.getBukkitEntity().getLocation().getBlockX(), entityPet.getBukkitEntity().getLocation().getBlockY(), entityPet.getBukkitEntity().getLocation().getBlockZ());
    }

    @Override
    public void setLocation(Location location)
    {
        entityPet.getBukkitEntity().teleport(location);
    }

    @Override
    public Player getOwner()
    {
        return this.petOwner;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public UUID getUUID()
    {
        return this.uuid;
    }

    @Override
    public void setUUID(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public void showName()
    {
        ArmorStand armourStand = (ArmorStand) entityPet.getBukkitEntity().getWorld().spawnEntity(this.getLocation(), EntityType.ARMOR_STAND);
        armourStand.setSmall(true);
        armourStand.setCustomName(name);
        armourStand.setCustomNameVisible(true);
        armourStand.setVisible(true);

    }

    @Override
    public abstract PetType getType();

    public void createPet()
    {
        Location location = getOwner().getPlayer().getLocation();
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityPet entityPet = getType().getNewEntityInstance(nmsWorld, this);
        this.entityPet = entityPet;

        entityPet.setLocation(location);

        Vowed.LOG.debug(entityPet.getBukkitEntity().getHandle().toString());
        nmsWorld.addEntity(entityPet.getBukkitEntity().getHandle());
    }
}

package me.vowed.api.pets;

import me.vowed.api.pets.player.PetPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by JPaul on 11/22/2015.
 */
public interface IPet
{

    Location getLocation();

    Location getBlockLocation();

    void setLocation(Location location);

    Player getOwner();

    abstract PetType getType();

    String getName();

    void setName(String name);

    void showName();

    UUID getUUID();

    void setUUID(UUID uuid);
}

package me.vowed.api.pets.player;

import me.vowed.api.pets.Pet;
import me.vowed.api.pets.PetList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by JPaul on 11/29/2015.
 */
public class PetPlayer
{
    protected final UUID playerUUID;
    Map<UUID, PetPlayer> petPlayerCache = new HashMap<>();

    protected PetPlayer()
    {
        this(UUID.randomUUID());
    }

    protected PetPlayer(UUID playerUUID)
    {
        this.playerUUID = playerUUID;
    }

    public Pet getPet()
    {
        return PetList.getPet(this);
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(playerUUID);
    }

    public PetPlayer createPetPlayer(Player player)
    {
        return new OnlinePetPlayer(player.getUniqueId());
    }
}

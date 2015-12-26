package me.vowed.api.pets;

import me.vowed.api.pets.petTypes.EntityWolfPet;
import me.vowed.api.pets.petTypes.PetWolf;
import me.vowed.api.pets.petTypes.Wolf;
import me.vowed.api.pets.player.OnlinePetPlayer;
import me.vowed.api.pets.player.PetPlayer;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Random;

/**
 * Created by JPaul on 11/22/2015.
 */
public class PetListener implements Listener
{
    @EventHandler
    public void on(PlayerDropItemEvent dropEvent)
    {
    }
}

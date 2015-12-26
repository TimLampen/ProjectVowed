package me.vowed.api.damage;

import me.vowed.api.items.Armour;
import me.vowed.api.plugin.Vowed;
import net.minecraft.server.v1_8_R3.Item;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JPaul on 10/2/2015.
 */
public class DamagePlayer implements Listener
{
    private boolean inDamageEvent;

    @EventHandler
    public void on(EntityDamageEvent damageEvent)
    {
        if (damageEvent.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
        {
            damageEvent.setDamage(.1); //Damage/IDamage handles damage now
        }
    }

    @EventHandler
    public void on(InventoryClickEvent clickEvent)
    {
    }
}

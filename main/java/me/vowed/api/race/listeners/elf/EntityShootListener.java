package me.vowed.api.race.listeners.elf;

import me.vowed.api.player.PlayerWrapper;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.plugin.VowedPlugin;
import me.vowed.api.race.Race;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Primary on 12/30/2015.
 */
public class EntityShootListener implements Listener{
    VowedPlugin p;
    public EntityShootListener(VowedPlugin plugin){
        this.p = plugin;
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent shootEvent)
    {
        if(shootEvent.getEntity() instanceof Player)
        {
            Vector velocity = shootEvent.getProjectile().getVelocity();
            shootEvent.getProjectile().remove();

            double speed = velocity.length();
            Vector direction = new Vector(velocity.getX() / speed, velocity.getY() / speed, velocity.getZ() / speed);
            double offSet = 3D;

            Arrow arrow = shootEvent.getEntity().launchProjectile(Arrow.class);
            arrow.setVelocity(new Vector(direction.getX() + (Math.random() - 0.5) / offSet, direction.getY() + (Math.random() - 0.5) / offSet, direction.getZ() + (Math.random() - 0.5) / offSet).normalize().multiply(speed));

        }
    }
}

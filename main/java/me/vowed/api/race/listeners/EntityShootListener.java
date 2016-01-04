package me.vowed.api.race.listeners;

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
    public void onShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player)event.getEntity();
            PlayerWrapper wrapper = new PlayerWrapper(player);
            Race race = wrapper.getRace();
            Random ran = new Random();

            double yaw = player.getLocation().getYaw(); //Get yaw of player, in degrees.
            double pitch = player.getLocation().getPitch(); //Get pitch, in degrees
            int variation = 10; //Variation in degrees
            int var2 = variation * 2; //Used later for randomization
            double variationYaw = ((Math.random() * var2) - variation); //Vary by between 10 and -10 degrees
            double variationPitch = ((Math.random() * var2) - variation);
            yaw+= variationYaw;
            pitch+=variationPitch;

//Get vector values from rotation using sine and cosine.
            double sinPitch = Math.sin(Math.toRadians(pitch));
            double sinYaw = Math.sin(Math.toRadians(yaw));
            double cosYaw = Math.cos(Math.toRadians(yaw));

            Vector arrowVelocity = new Vector(sinYaw, sinPitch, cosYaw);
            arrowVelocity.normalize(); //Probably not necessary, but good practise.
            event.getProjectile().setVelocity(arrowVelocity);
        }
    }
}

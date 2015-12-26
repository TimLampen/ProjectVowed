package me.vowed.api.test;

import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import me.vowed.api.plugin.Vowed;
import me.vowed.api.plugin.VowedPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 11/8/2015.
 */
public class Shield
{
    Player player;

    public Shield(Player player)
    {
        this.player = player;
    }


    public void start()
    {
        ShieldEffect effect = new ShieldEffect(VowedPlugin.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation(player.getLocation().subtract(0, .2, 0)));
        effect.sphere = true;
        effect.duration = 10 * 1000; //convert to seconds
        effect.particles = 50;
        effect.particle = ParticleEffect.DRIP_LAVA;
        effect.radius = 3;
        effect.speed = 200;
        effect.start();
        Vowed.LOG.info(String.valueOf(effect.duration / 300));

        new BukkitRunnable()
        {

            @Override
            public void run()
            {


                List<Entity> entityList = getNearbyEntities(effect.getLocation(), 4);
                entityList.remove(player);

                if (!entityList.isEmpty())
                {
                    for (Entity entity : entityList)
                    {
                        if (entity instanceof Arrow)
                        {
                            Arrow arrow = (Arrow) entity;
                            arrow.setCritical(false);
                            arrow.setVelocity(player.getLocation().getDirection().multiply(-.2));
                        }
                    }

                }

                if (effect.isDone())

                {
                    cancel();
                }
            }
        }.runTaskTimer(Vowed.getPlugin(), 0, 0);
    }

    public void pushAwayEntity(Location center, Entity entity, double speed)
    {
        Vector unitVector = entity.getLocation().toVector().subtract(center.toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }

    public static List<Entity> getNearbyEntities(Location location, double radius)
    {
        double chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        List<Entity> radiusEntities = new ArrayList<>();
        for (double chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (double chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) location.getX(), y = (int) location.getY(), z = (int) location.getZ();
                for (Entity entity : new Location(location.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities())
                {
                    if (entity.getLocation().distance(location) <= radius && entity.getLocation().getBlock() != location.getBlock())
                        radiusEntities.add(entity);
                }
            }
        }
        return radiusEntities;
    }

    public static List<Entity> getNearbyEntities(Player player, double radius)
    {
        return getNearbyEntities(player.getLocation(), radius);
    }
}


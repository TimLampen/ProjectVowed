package me.vowed.api.health;

import me.vowed.api.plugin.Vowed;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by JPaul on 10/3/2015.
 */
public class Health implements IHealth
{
    private int playerOldHP;
    private int playerMaxHP;
    Player player;

    public Health(Entity source, int maxHealth)
    {
        this.player = (Player) source;
        this.playerMaxHP = maxHealth;
    }

    public Health()
    {

    }

    @Override
    public int getHealth()
    {
        return this.playerOldHP;
    }

    @Override
    public int getMaxHealth()
    {
        return this.playerMaxHP;
    }

    @Override
    public void setHealth(int health)
    {
        this.playerOldHP = health;
    }

    @Override
    public void setMaxHealth(int maxHealth)
    {
        this.playerMaxHP = maxHealth;
    }

    @Override
    public void convertHealth()
    {
        {
            double playerNewHP = this.playerOldHP - Vowed.getDamageManager().getDamage();
            double healthPercent = playerNewHP / this.playerMaxHP;
            int healthDisplay = (int) (healthPercent * 20D);
            Vowed.LOG.info(String.valueOf(healthDisplay));
            player.setHealth(healthDisplay);
            this.playerOldHP = (int) playerNewHP;
        }
    }
}

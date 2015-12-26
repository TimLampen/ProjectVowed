package me.vowed.api.health;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

/**
 * Created by JPaul on 10/3/2015.
 */
public interface IHealth
{
    int getHealth();

    int getMaxHealth();

    void setHealth(int health);

    void setMaxHealth(int maxHealth);

    void convertHealth();
}

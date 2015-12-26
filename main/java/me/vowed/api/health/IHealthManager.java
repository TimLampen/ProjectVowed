package me.vowed.api.health;

import org.bukkit.entity.Player;

/**
 * Created by JPaul on 11/14/2015.
 */
public interface IHealthManager
{
    IHealth getHealth(Player player);

    void setHealth(Player player, IHealth health);
}

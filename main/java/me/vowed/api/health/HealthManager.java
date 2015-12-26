package me.vowed.api.health;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by JPaul on 11/14/2015.
 */
public class HealthManager implements IHealthManager
{
    private HashMap<Player, IHealth> health = new HashMap<>();

    @Override
    public IHealth getHealth(Player player)
    {
        return this.health.get(player);
    }

    @Override
    public void setHealth(Player player, IHealth health)
    {
        this.health.put(player, health);
    }
}

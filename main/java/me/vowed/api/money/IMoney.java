package me.vowed.api.money;

import org.bukkit.entity.Player;

/**
 * Created by JPaul on 11/13/2015.
 */
public interface IMoney
{
    double getAmount();

    void setAmount(double amount);

    void add(double amount);

    void subtract(double amount);
}

package me.vowed.api.race.races;

import me.vowed.api.money.currency.CurrencyType;
import me.vowed.api.race.Race;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Created by JPaul on 11/17/2015.
 */
public class Human extends Race
{
    String name;
    Gender gender;

    public Human(Gender gender)
    {
        super(RaceType.HUMAN, gender);
        this.name = "Human";
        this.gender = gender;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public UUID getSkin()
    {
        return null;
    }

}

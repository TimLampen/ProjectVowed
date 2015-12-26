package me.vowed.api.pets.petTypes;

import me.vowed.api.pets.Pet;
import me.vowed.api.pets.PetType;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 11/30/2015.
 */
public class PetWolf extends Pet
{
    public PetWolf(Player owner)
    {
        super(owner);
    }

    @Override
    public PetType getType()
    {
        return PetType.Wolf;
    }
}

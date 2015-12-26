package me.vowed.api.pets;

import me.vowed.api.pets.player.PetPlayer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;

/**
 * Created by JPaul on 11/29/2015.
 */
public interface IPetEntity extends Creature
{
    public Pet getPet();

    public EntityPet getHandle();

    public boolean canMove();

    public PetType getPetType();

    public Player getOwner();
}

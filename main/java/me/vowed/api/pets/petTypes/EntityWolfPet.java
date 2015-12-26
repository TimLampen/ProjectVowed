package me.vowed.api.pets.petTypes;

import me.vowed.api.pets.EntityPet;
import me.vowed.api.pets.IPet;
import me.vowed.api.pets.Pet;
import me.vowed.api.pets.PetType;
import me.vowed.api.pets.ai.movement.FollowOwner;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

/**
 * Created by JPaul on 11/22/2015.
 */
public class EntityWolfPet extends EntityPet
{
    public EntityWolfPet(World world, Pet pet)
    {
        super(world, pet);
    }
}
